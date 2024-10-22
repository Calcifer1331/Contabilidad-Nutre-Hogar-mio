INSERT INTO tipo_documento (nombre)
VALUES ('FACTURA');
INSERT INTO tipo_documento (nombre)
VALUES ('RECIBO');
INSERT INTO tipo_documento (nombre)
VALUES ('NOTA DE CREDITO');
INSERT INTO tipo_cuenta(nombre)
VALUES ('ACTIVOS');
INSERT INTO tipo_cuenta(nombre)
VALUES ('PASIVOS');
INSERT INTO tipo_cuenta(nombre)
VALUES ('PATRIMONIO');
INSERT INTO tipo_cuenta(nombre)
VALUES ('INGRESOS');
INSERT INTO tipo_cuenta(nombre)
VALUES ('GASTOS');
INSERT INTO sub_tipo_cuenta(id, nombre, id_tipo_cuenta)
VALUES ('1.1', 'ACTIVOS CORRIENTES', 1);
INSERT INTO sub_tipo_cuenta(id, nombre, id_tipo_cuenta)
VALUES ('1.3', 'INVENTARIOS', 2);
INSERT INTO sub_tipo_cuenta(id, nombre, id_tipo_cuenta)
VALUES ('2.1', 'CUENTAS POR PAGAR', 3);
INSERT INTO sub_tipo_cuenta(id, nombre, id_tipo_cuenta)
VALUES ('2.2', 'OTRAS', 4);
INSERT INTO cuenta (id, nombre, id_sub_tipo_cuenta)
VALUES ('1000', 'Caja', '1.1');
INSERT INTO cuenta (id, nombre, id_sub_tipo_cuenta)
VALUES ('1100', 'Bancos', '1.1');
INSERT INTO cuenta (id, nombre, id_sub_tipo_cuenta)
VALUES ('1200', 'Cuenta por Cobrar', '1.1');
INSERT INTO cuenta (id, nombre, id_sub_tipo_cuenta)
VALUES ('1300', 'Inventarios', '1.3');
INSERT INTO cuenta (id, nombre, id_sub_tipo_cuenta)
VALUES ('2000', 'Cuenta por Pagar', '2.1');
INSERT INTO cuenta (id, nombre, id_sub_tipo_cuenta)
VALUES ('2100', 'Préstamos Bancarios', '2.1');
INSERT INTO cuenta (id, nombre, id_sub_tipo_cuenta)
VALUES ('3000', 'Capital Social', '1.1');
INSERT INTO cuenta (id, nombre, id_sub_tipo_cuenta)
VALUES ('3100', 'Reservas de Capital', '1.1');
INSERT INTO cuenta (id, nombre, id_sub_tipo_cuenta)
VALUES ('4000', 'Ventas', '1.1');
INSERT INTO cuenta (id, nombre, id_sub_tipo_cuenta)
VALUES ('4100', 'Ingresos por Servicios', '1.1');
INSERT INTO cuenta (id, nombre, id_sub_tipo_cuenta)
VALUES ('5000', 'Gastos de Sueldos', '1.1');
INSERT INTO cuenta (id, nombre, id_sub_tipo_cuenta)
VALUES ('5100', 'Gastos de Alquiler', '1.1');
INSERT INTO cuenta (id, nombre, id_sub_tipo_cuenta)
VALUES ('5200', 'Gastos de Suministros', '1.1');
INSERT INTO cuenta (id, nombre, id_sub_tipo_cuenta)
VALUES ('5300', 'Gastos de Publicidad', '1.1');
INSERT INTO cuenta (id, nombre, id_sub_tipo_cuenta)
VALUES ('6000', 'Depreciación', '1.1');
INSERT INTO cuenta (id, nombre, id_sub_tipo_cuenta)
VALUES ('7000', 'Intereses Pagados', '1.1');
INSERT INTO cuenta (id, nombre, id_sub_tipo_cuenta)
VALUES ('8000', 'Impuestos sobre la Renta', '1.1');
INSERT INTO cuenta (id, nombre, id_sub_tipo_cuenta)
VALUES ('9000', 'Otros Ingresos', '1.1');
INSERT INTO cuenta (id, nombre, id_sub_tipo_cuenta)
VALUES ('9100', 'Otros Gastos', '1.1');
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Aporte de capital inicial por los socios', 1642118400000, 1);
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Compra de inventario de mercaderías', 1706227200000, 1);
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Venta de productos a clientes', 1706486400000, 1);
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Pago de sueldos del personal', 1715817600000, 1);
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Pago de alquiler del local comercial', 1705968000000, 1);
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Compra de equipo de oficina', 1704412800000, 1);
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Venta de servicios de consultoría', 1704240000000, 1);
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Pago de intereses de préstamo bancario', 1728432000000, 1);
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Pago de impuestos sobre la renta', 1711065600000, 2);
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Ingreso por venta de activos fijos', 1704240000000, 2);
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Compra de suministros de oficina', 1674691200000, 2);
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Gastos de publicidad en medios locales', 1672963200000, 2);
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Depreciación mensual de activos fijos', 1674086400000, 2);
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Pago de préstamo bancario a largo plazo', 1610064000000, 2);
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Ingreso por otros servicios prestados', 1705104000000, 3);
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Pago de gastos de suministros', 1672963200000, 3);
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Ingreso por otros ingresos no operativos', 1706227200000, 3);
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Pago de otros gastos operativos', 1611187200000, 3);
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Compra de publicidad en línea', 1706486400000, 3);
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Pago de impuestos adicionales', 1609459200000, 3);
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (11, 'ryguhjk', 'Aporte de capital inicial', 1000, 768, 0);
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (10, 'tfygujhkl', 'Compra de inventario', 1300, 6457, 0);
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (10, 'yghjk', 'Venta de productos', 4000, 0, 46);
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (9, 'ygjhkml', 'Pago de sueldos', 5000, 43, 0);
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (9, 'gjhk', 'Pago de alquiler', 5100, 0, 7);
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (8, 'uhjkl', 'Compra de equipo de oficina', 1200, 57, 0);
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (8, 'uhjkm', 'Venta de servicios de consultoría', 4100, 0, 465);
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (7, 'ygjhk', 'Pago de intereses de préstamo', 7000, 64, 0);
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (6, 'tygjk', 'Pago de impuestos sobre la renta', 8000, 46, 0);
-- Nuevos registros en la tabla "asiento"
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Compra de vehículo para transporte', 1730265600000, 1);
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Ingreso por alquiler de oficinas', 1730265600000, 1);
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Pago de servicios públicos', 1730265600000, 2);
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Pago de dividendos a los accionistas', 1730265600000, 2);
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Compra de materiales para producción', 1730265600000, 3);
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Venta de equipo de cómputo', 1730265600000, 3);

-- Nuevos registros en la tabla "registro"
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (12, 'abc123', 'Compra de vehículo', 1200, 20000, 0);
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (12, 'abc124', 'Préstamos bancarios', 2100, 0, 20000);
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (13, 'xyz456', 'Ingreso por alquiler', 4000, 0, 1500);
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (14, 'xyz789', 'Pago de servicios públicos', 5000, 500, 0);
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (14, 'xyz790', 'Cuenta por pagar', 2000, 0, 500);
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (15, 'def456', 'Pago de dividendos', 3000, 3000, 0);
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (15, 'def457', 'Cuenta por pagar', 2000, 0, 3000);
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (16, 'ghi789', 'Compra de materiales', 1300, 10000, 0);
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (16, 'ghi790', 'Cuenta por pagar', 2000, 0, 10000);
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (17, 'jkl123', 'Venta de equipo de cómputo', 4000, 0, 3000);
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (17, 'jkl124', 'Depreciación de activos', 6000, 500, 0);

-- Nuevos asientos
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Pago de comisiones a vendedores', 1732156800000, 1);
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Devolución de mercaderías por clientes', 1732156800000, 1);
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Pago de intereses sobre bonos emitidos', 1732156800000, 2);
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Compra de software de gestión', 1732156800000, 2);
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Ingreso por venta de acciones', 1732156800000, 3);
INSERT INTO asiento (concepto, fecha, id_tipo_documento)
VALUES ('Pago de primas de seguros', 1732156800000, 3);

-- Nuevos registros en la tabla "registro"
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (18, 'mno345', 'Pago de comisiones', 5000, 1200, 0);
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (18, 'mno346', 'Bancos', 1100, 0, 1200);
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (19, 'pqr567', 'Devolución de mercaderías', 1300, 500, 0);
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (19, 'pqr568', 'Ventas', 4000, 0, 500);
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (20, 'stu890', 'Pago de intereses', 7000, 300, 0);
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (20, 'stu891', 'Préstamos Bancarios', 2100, 0, 300);
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (21, 'vwx123', 'Compra de software', 1200, 4500, 0);
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (21, 'vwx124', 'Cuenta por pagar', 2000, 0, 4500);
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (22, 'yza456', 'Ingreso por venta de acciones', 3000, 0, 10000);
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (22, 'yza457', 'Capital Social', 3100, 10000, 0);
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (23, 'bcd789', 'Pago de primas de seguros', 5100, 2000, 0);
INSERT INTO registro (id_asiento, comprobante, referencia, id_cuenta, debe, haber)
VALUES (23, 'bcd790', 'Bancos', 1100, 0, 2000);
