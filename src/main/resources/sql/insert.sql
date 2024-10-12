INSERT INTO Cuentas (codigo_cuenta, descripcion, nombre_cuenta, tipo_cuenta) VALUES ('1000', 'Caja general de la empresa', 'Caja', 'ACTIVO');
INSERT INTO Cuentas (codigo_cuenta, descripcion, nombre_cuenta, tipo_cuenta) VALUES ('1100', 'Bancos - Cuenta corriente', 'Bancos', 'ACTIVO');
INSERT INTO Cuentas (codigo_cuenta, descripcion, nombre_cuenta, tipo_cuenta) VALUES ('1200', 'Cuentas por cobrar clientes', 'Cuentas por Cobrar', 'ACTIVO');
INSERT INTO Cuentas (codigo_cuenta, descripcion, nombre_cuenta, tipo_cuenta) VALUES ('1300', 'Inventarios de mercaderías', 'Inventarios', 'ACTIVO');
INSERT INTO Cuentas (codigo_cuenta, descripcion, nombre_cuenta, tipo_cuenta) VALUES ('2000', 'Cuentas por pagar proveedores', 'Cuentas por Pagar', 'PASIVO');
INSERT INTO Cuentas (codigo_cuenta, descripcion, nombre_cuenta, tipo_cuenta) VALUES ('2100', 'Préstamos bancarios a corto plazo', 'Préstamos Bancarios', 'PASIVO');
INSERT INTO Cuentas (codigo_cuenta, descripcion, nombre_cuenta, tipo_cuenta) VALUES ('3000', 'CAPITAL social aportado', 'CAPITAL Social', 'CAPITAL');
INSERT INTO Cuentas (codigo_cuenta, descripcion, nombre_cuenta, tipo_cuenta) VALUES ('3100', 'Reservas de capital', 'Reservas de CAPITAL', 'CAPITAL');
INSERT INTO Cuentas (codigo_cuenta, descripcion, nombre_cuenta, tipo_cuenta) VALUES ('4000', 'Ventas de productos', 'Ventas', 'INGRESO');
INSERT INTO Cuentas (codigo_cuenta, descripcion, nombre_cuenta, tipo_cuenta) VALUES ('4100', 'INGRESOs por servicios', 'INGRESOs por Servicios', 'INGRESO');
INSERT INTO Cuentas (codigo_cuenta, descripcion, nombre_cuenta, tipo_cuenta) VALUES ('5000', 'GASTOs de sueldos', 'GASTOs de Sueldos', 'GASTO');
INSERT INTO Cuentas (codigo_cuenta, descripcion, nombre_cuenta, tipo_cuenta) VALUES ('5100', 'GASTOs de alquiler', 'GASTOs de Alquiler', 'GASTO');
INSERT INTO Cuentas (codigo_cuenta, descripcion, nombre_cuenta, tipo_cuenta) VALUES ('5200', 'GASTOs de suministros', 'GASTOs de Suministros', 'GASTO');
INSERT INTO Cuentas (codigo_cuenta, descripcion, nombre_cuenta, tipo_cuenta) VALUES ('5300', 'GASTOs de publicidad', 'GASTOs de Publicidad', 'GASTO');
INSERT INTO Cuentas (codigo_cuenta, descripcion, nombre_cuenta, tipo_cuenta) VALUES ('6000', 'Depreciación de activos fijos', 'Depreciación', 'GASTO');
INSERT INTO Cuentas (codigo_cuenta, descripcion, nombre_cuenta, tipo_cuenta) VALUES ('7000', 'Intereses pagados', 'Intereses Pagados', 'GASTO');
INSERT INTO Cuentas (codigo_cuenta, descripcion, nombre_cuenta, tipo_cuenta) VALUES ('8000', 'Impuestos sobre la renta', 'Impuestos sobre la Renta', 'GASTO');
INSERT INTO Cuentas (codigo_cuenta, descripcion, nombre_cuenta, tipo_cuenta) VALUES ('9000', 'Otros ingresos', 'Otros INGRESOs', 'INGRESO');
INSERT INTO Cuentas (codigo_cuenta, descripcion, nombre_cuenta, tipo_cuenta) VALUES ('9100', 'Otros gastos', 'Otros GASTOs', 'GASTO');
INSERT INTO Transacciones (concepto, fecha, tipo_documento) VALUES ('Aporte de capital inicial por los socios', 1642118400000, 'AJUSTE');
INSERT INTO Transacciones (concepto, fecha, tipo_documento) VALUES ('Compra de inventario de mercaderías', 1706227200000, 'AJUSTE');
INSERT INTO Transacciones (concepto, fecha, tipo_documento) VALUES ('Venta de productos a clientes', 1706486400000, 'AJUSTE');
INSERT INTO Transacciones (concepto, fecha, tipo_documento) VALUES ('Pago de sueldos del personal', 1715817600000, 'AJUSTE');
INSERT INTO Transacciones (concepto, fecha, tipo_documento) VALUES ('Pago de alquiler del local comercial', 1705968000000, 'AJUSTE');
INSERT INTO Transacciones (concepto, fecha, tipo_documento) VALUES ('Compra de equipo de oficina', 1704412800000, 'AJUSTE');
INSERT INTO Transacciones (concepto, fecha, tipo_documento) VALUES ('Venta de servicios de consultoría', 1704240000000, 'AJUSTE');
INSERT INTO Transacciones (concepto, fecha, tipo_documento) VALUES ('Pago de intereses de préstamo bancario', 1728432000000, 'AJUSTE');
INSERT INTO Transacciones (concepto, fecha, tipo_documento) VALUES ('Pago de impuestos sobre la renta', 1711065600000, 'EGRESO');
INSERT INTO Transacciones (concepto, fecha, tipo_documento) VALUES ('INGRESO por venta de activos fijos', 1704240000000, 'EGRESO');
INSERT INTO Transacciones (concepto, fecha, tipo_documento) VALUES ('Compra de suministros de oficina', 1674691200000, 'EGRESO');
INSERT INTO Transacciones (concepto, fecha, tipo_documento) VALUES ('GASTOs de publicidad en medios locales', 1672963200000, 'EGRESO');
INSERT INTO Transacciones (concepto, fecha, tipo_documento) VALUES ('Depreciación mensual de activos fijos', 1674086400000, 'EGRESO');
INSERT INTO Transacciones (concepto, fecha, tipo_documento) VALUES ('Pago de préstamo bancario a largo plazo', 1610064000000, 'EGRESO');
INSERT INTO Transacciones (concepto, fecha, tipo_documento) VALUES ('INGRESO por otros servicios prestados', 1705104000000, 'INGRESO');
INSERT INTO Transacciones (concepto, fecha, tipo_documento) VALUES ('Pago de gastos de suministros', 1672963200000, 'INGRESO');
INSERT INTO Transacciones (concepto, fecha, tipo_documento) VALUES ('INGRESO por otros ingresos no operativos', 1706227200000, 'INGRESO');
INSERT INTO Transacciones (concepto, fecha, tipo_documento) VALUES ('Pago de otros gastos operativos', 1611187200000, 'INGRESO');
INSERT INTO Transacciones (concepto, fecha, tipo_documento) VALUES ('Compra de publicidad en línea', 1706486400000, 'INGRESO');
INSERT INTO Transacciones (concepto, fecha, tipo_documento) VALUES ('Pago de impuestos adicionales', 1609459200000,'INGRESO');
INSERT INTO Asientos (id_transaccion,comprobante, referencia, id_cuenta, debe, haber) VALUES (11,'ryguhjk','Aporte de capital inicial', 1, 1,768);
INSERT INTO Asientos (id_transaccion,comprobante, referencia, id_cuenta, debe, haber) VALUES (10,'tfygujhkl','Compra de inventario', 2, 2,6457);
INSERT INTO Asientos (id_transaccion,comprobante, referencia, id_cuenta, debe, haber) VALUES (10,'yghjk','Venta de productos', 3, 3,46);
INSERT INTO Asientos (id_transaccion,comprobante, referencia, id_cuenta, debe, haber) VALUES (9,'ygjhkml','Pago de sueldos', 4, 4,43);
INSERT INTO Asientos (id_transaccion,comprobante, referencia, id_cuenta, debe, haber) VALUES (9,'gjhk','Pago de alquiler', 1, 5,7);
INSERT INTO Asientos (id_transaccion,comprobante, referencia, id_cuenta, debe, haber) VALUES (8,'uhjkl','Compra de equipo de oficina', 2, 6,57);
INSERT INTO Asientos (id_transaccion,comprobante, referencia, id_cuenta, debe, haber) VALUES (8,'uhjkm','Venta de servicios de consultoría', 2, 7,465);
INSERT INTO Asientos (id_transaccion,comprobante, referencia, id_cuenta, debe, haber) VALUES (7,'ygjhk','Pago de intereses de préstamo', 3, 8,8);
INSERT INTO Asientos (id_transaccion,comprobante, referencia, id_cuenta, debe, haber) VALUES (7,'guhjkml,','Pago de impuestos', 3, 9,87);
INSERT INTO Asientos (id_transaccion,comprobante, referencia, id_cuenta, debe, haber) VALUES (6,'ygjhkn','INGRESO por venta de activos', 4, 10,76);
INSERT INTO Asientos (id_transaccion,comprobante, referencia, id_cuenta, debe, haber) VALUES (6,'ygujhk','Compra de suministros de oficina', 5, 11,76);
INSERT INTO Asientos (id_transaccion,comprobante, referencia, id_cuenta, debe, haber) VALUES (5,'ghjnkm','GASTOs de publicidad', 4, 12,87);
INSERT INTO Asientos (id_transaccion,comprobante, referencia, id_cuenta, debe, haber) VALUES (4,'tfyghjk','Depreciación de activos fijos', 10, 13,678);
INSERT INTO Asientos (id_transaccion,comprobante, referencia, id_cuenta, debe, haber) VALUES (4,'rtyguhjk','Pago de préstamo a largo plazo', 9, 14,877);
INSERT INTO Asientos (id_transaccion,comprobante, referencia, id_cuenta, debe, haber) VALUES (3,'tyghj','INGRESO por otros servicios', 9, 15,8);
INSERT INTO Asientos (id_transaccion,comprobante, referencia, id_cuenta, debe, haber) VALUES (3,'redtfyghj','Pago de gastos de suministros', 6, 16,567);
INSERT INTO Asientos (id_transaccion,comprobante, referencia, id_cuenta, debe, haber) VALUES (2,'tryfghj','INGRESO por otros ingresos', 6, 17,65);
INSERT INTO Asientos (id_transaccion,comprobante, referencia, id_cuenta, debe, haber) VALUES (2,'hgjbm','Pago de otros gastos operativos', 6, 18,657);
INSERT INTO Asientos (id_transaccion,comprobante, referencia, id_cuenta, debe, haber) VALUES (1,'ugj','Compra de publicidad en línea', 7, 19,548);
INSERT INTO Asientos (id_transaccion,comprobante, referencia, id_cuenta, debe, haber) VALUES (1,'ryguj','Pago de impuestos adicionales', 6, 20,76);