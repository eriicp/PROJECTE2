const mysql = require('mysql2/promise');
const stripe = require('stripe')(process.env.STRIPE_SECRET_KEY);

exports.handler = async (event) => {
    // 1. Conexión a tu base de datos RDS
    const connection = await mysql.createConnection({
        host: process.env.DB_HOST,
        user: process.env.DB_USER,
        password: process.env.DB_PASSWORD,
        database: process.env.DB_NAME
    });

    try {
        // 2. Buscar transferencias pendientes de liberar (fecha pasada y estado 'retenido')
        const [rows] = await connection.execute(`
            SELECT le.id_escrow, le.id_transaccion, t.monto_base, u.stripe_account_id 
            FROM liquidaciones_escrow le
            JOIN transacciones t ON le.id_transaccion = t.id_transaccion
            JOIN entradas e ON t.id_entrada = e.id_entrada
            JOIN usuarios u ON e.id_vendedor = u.id_usuario
            WHERE le.estado_fondos = 'retenido' 
            AND le.fecha_liberacion_prevista <= NOW()
        `);

        console.log(`Encontrados ${rows.length} pagos listos para liberar.`);

        // 3. Procesar cada pago
        for (const liquidacion of rows) {
            try {
                // Hacer la transferencia en Stripe hacia el vendedor
                const transfer = await stripe.transfers.create({
                    amount: Math.round(liquidacion.monto_base * 100), // Stripe maneja céntimos
                    currency: 'eur',
                    destination: liquidacion.stripe_account_id,
                    description: `Liquidación por venta de entrada. Transacción: ${liquidacion.id_transaccion}`,
                });

                // Actualizar la base de datos a 'liquidado' y guardar el ID de transferencia de Stripe
                await connection.execute(`
                    UPDATE liquidaciones_escrow 
                    SET estado_fondos = 'liquidado', stripe_transfer_id = ?
                    WHERE id_escrow = ?
                `, [transfer.id, liquidacion.id_escrow]);

                console.log(`Pago ${liquidacion.id_escrow} liquidado con éxito.`);

            } catch (stripeError) {
                console.error(`Error procesando pago ${liquidacion.id_escrow}:`, stripeError.message);
                // Aquí podrías implementar alertas si un pago falla
            }
        }
    } catch (error) {
        console.error('Error en la base de datos:', error);
    } finally {
        await connection.end();
    }

    return { statusCode: 200, body: 'Proceso Escrow finalizado' };
};