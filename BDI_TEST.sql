Create table UGTP_TBL_ZONA_TARIFA(
ID_ZONA number GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
CLAVE_ZONA VARCHAR2(20) UNIQUE NOT NULL);


CREATE TABLE UGPT_TBL_NODO_COMERCIAL(
ID_NODO NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
CLAVE_NODO VARCHAR2(20) NOT NULL  UNIQUE,
DESCRIPCION VARCHAR2(100) NOT NULL
);


CREATE TABLE UGTP_TBL_USUARIO(
ID_USUARIO NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
NOMBRE VARCHAR2(200)  NOT NULL UNIQUE
);

CREATE TABLE UGTP_TBL_CONTRATO(
ID_CONTRATO NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
CLAVE_CONTRATO VARCHAR(200) UNIQUE NOT NULL,
ID_USUARIO NUMBER NOT NULL,
CONSTRAINT FK_CONTRATO_USUARIO FOREIGN KEY(ID_USUARIO)
REFERENCES UGTP_TBL_USUARIO (ID_USUARIO)
);

DROP TABLE UGTP_TBL_KARDEX;

CREATE TABLE UGTP_TBL_KARDEX(
ID_KARDEX NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
FECHA DATE NOT NULL,
ID_CONTRATO NUMBER NOT NULL,
ID_NODO_RECEPCION NUMBER NOT NULL,
ID_NODO_ENTREGA NUMBER NOT NULL,
ID_ZONA_INYECCION NUMBER NOT NULL,
ID_ZONA_EXTRACCION NUMBER NOT NULL,
QTY_NOM_RECEPCION NUMBER(18,3),
QTY_ASIG_RECEPCION NUMBER(18,3),
QTY_NOM_ENTREGA NUMBER(18,3),
QTY_ASIG_ENTREGA NUMBER(18, 3),
GAS_EXCESO NUMBER(18, 3),

TARIFA_EXCESO_FIRME NUMBER(18,6),
TARIFA_USO_INTERRRUMPIBLE NUMBER (18,6),
CARGO_USO NUMBER(18,2),
CARGO_GAS_EXCESO NUMBER (18,2),
TOTAL_FACTURAR NUMBER (18,2),
  CONSTRAINT FK_KARDEX_CONTRATO       FOREIGN KEY (ID_CONTRATO)
        REFERENCES UGTP_TBL_CONTRATO      (ID_CONTRATO),
    CONSTRAINT FK_KARDEX_NODO_REC       FOREIGN KEY (ID_NODO_RECEPCION)
        REFERENCES UGTP_TBL_NODO_COMERCIAL (ID_NODO),
    CONSTRAINT FK_KARDEX_NODO_ENT       FOREIGN KEY (ID_NODO_ENTREGA)
        REFERENCES UGTP_TBL_NODO_COMERCIAL (ID_NODO),
    CONSTRAINT FK_KARDEX_ZONA_INY       FOREIGN KEY (ID_ZONA_INYECCION)
        REFERENCES UGTP_TBL_ZONA_TARIFA  (ID_ZONA),
    CONSTRAINT FK_KARDEX_ZONA_EXT       FOREIGN KEY (ID_ZONA_EXTRACCION)
        REFERENCES UGTP_TBL_ZONA_TARIFA  (ID_ZONA)
);


CREATE INDEX IDX_KARDEX_CONTRATO   ON UGTP_TBL_KARDEX (ID_CONTRATO);
CREATE INDEX IDX_KARDEX_FECHA      ON UGTP_TBL_KARDEX (FECHA);
CREATE INDEX IDX_KARDEX_NODO_REC   ON UGTP_TBL_KARDEX (ID_NODO_RECEPCION);
CREATE INDEX IDX_KARDEX_NODO_ENT   ON UGTP_TBL_KARDEX (ID_NODO_ENTREGA);
CREATE INDEX IDX_KARDEX_ZONA_INY   ON UGTP_TBL_KARDEX (ID_ZONA_INYECCION);
CREATE INDEX IDX_KARDEX_ZONA_EXT   ON UGTP_TBL_KARDEX (ID_ZONA_EXTRACCION);

SELECT table_name FROM user_tables WHERE table_name LIKE 'UGTP_TBL%';


CREATE OR REPLACE PROCEDURE UGTP_SP_CARGA_ZONA (
    p_clave IN UGTP_TBL_ZONA_TARIFA.CLAVE_ZONA%TYPE
) AS
    v_count NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO v_count
    FROM UGTP_TBL_ZONA_TARIFA
    WHERE CLAVE_ZONA = p_clave;

    IF v_count = 0 THEN
        INSERT INTO UGTP_TBL_ZONA_TARIFA (CLAVE_ZONA)
        VALUES (p_clave);
    END IF;
END UGTP_SP_CARGA_ZONA;


CREATE OR REPLACE PROCEDURE UGTP_SP_CARGA_NODO (
    p_clave IN UGTP_TBL_NODO_COMERCIAL.CLAVE_NODO%TYPE,
    p_desc  IN UGTP_TBL_NODO_COMERCIAL.DESCRIPCION%TYPE
) AS
BEGIN
    MERGE INTO UGTP_TBL_NODO_COMERCIAL t
    USING (SELECT p_clave AS clave, p_desc AS descripcion FROM dual) src
    ON (t.CLAVE_NODO = src.clave)

    WHEN MATCHED THEN
        UPDATE SET t.DESCRIPCION = src.descripcion

    WHEN NOT MATCHED THEN
        INSERT (CLAVE_NODO, DESCRIPCION)
        VALUES (src.clave, src.descripcion);

END UGTP_SP_CARGA_NODO;




CREATE OR REPLACE PROCEDURE UGTP_SP_CARGA_FILA (
    p_fecha                   IN DATE,
    p_clave_contrato          IN VARCHAR2,
    p_nombre_usuario          IN VARCHAR2,
    p_clave_nodo_rec          IN VARCHAR2,
    p_desc_nodo_rec           IN VARCHAR2,
    p_clave_nodo_ent          IN VARCHAR2,
    p_desc_nodo_ent           IN VARCHAR2,
    p_clave_zona_iny          IN VARCHAR2,
    p_clave_zona_ext          IN VARCHAR2,
    p_qty_nom_rec             IN NUMBER,
    p_qty_asig_rec            IN NUMBER,
    p_qty_nom_ent             IN NUMBER,
    p_qty_asig_ent            IN NUMBER,
    p_gas_exceso              IN NUMBER,
    p_tarifa_exc_firme        IN NUMBER,
    p_tarifa_uso_int          IN NUMBER,
    p_cargo_uso               IN NUMBER,
    p_cargo_gas_exc           IN NUMBER,
    p_total_facturar          IN NUMBER
) AS
    v_id_usuario     UGTP_TBL_USUARIO.ID_USUARIO%TYPE;
    v_id_contrato    UGTP_TBL_CONTRATO.ID_CONTRATO%TYPE;
    v_id_nodo_rec    UGTP_TBL_NODO_COMERCIAL.ID_NODO%TYPE;
    v_id_nodo_ent    UGTP_TBL_NODO_COMERCIAL.ID_NODO%TYPE;
    v_id_zona_iny    UGTP_TBL_ZONA_TARIFA.ID_ZONA%TYPE;
    v_id_zona_ext    UGTP_TBL_ZONA_TARIFA.ID_ZONA%TYPE;
BEGIN
    -- 1. Catálogos
    UGTP_SP_CARGA_USUARIO(p_nombre_usuario);
    UGTP_SP_CARGA_NODO(p_clave_nodo_rec, p_desc_nodo_rec);
    UGTP_SP_CARGA_NODO(p_clave_nodo_ent, p_desc_nodo_ent);
    UGTP_SP_CARGA_ZONA(p_clave_zona_iny);
    UGTP_SP_CARGA_ZONA(p_clave_zona_ext);

    -- 2. Resolución de IDs
    SELECT ID_USUARIO INTO v_id_usuario
      FROM UGTP_TBL_USUARIO WHERE NOMBRE = p_nombre_usuario;

    SELECT ID_NODO INTO v_id_nodo_rec
      FROM UGTP_TBL_NODO_COMERCIAL WHERE CLAVE_NODO = p_clave_nodo_rec;

    SELECT ID_NODO INTO v_id_nodo_ent
      FROM UGTP_TBL_NODO_COMERCIAL WHERE CLAVE_NODO = p_clave_nodo_ent;

    SELECT ID_ZONA INTO v_id_zona_iny
      FROM UGTP_TBL_ZONA_TARIFA WHERE CLAVE_ZONA = p_clave_zona_iny;

    SELECT ID_ZONA INTO v_id_zona_ext
      FROM UGTP_TBL_ZONA_TARIFA WHERE CLAVE_ZONA = p_clave_zona_ext;

    -- 3. Contrato (MERGE para no duplicar)
    MERGE INTO UGTP_TBL_CONTRATO c
    USING (SELECT p_clave_contrato AS CLAVE FROM DUAL) src
       ON (c.CLAVE_CONTRATO = src.CLAVE)
    WHEN NOT MATCHED THEN
        INSERT (CLAVE_CONTRATO, ID_USUARIO)
        VALUES (p_clave_contrato, v_id_usuario);

    SELECT ID_CONTRATO INTO v_id_contrato
      FROM UGTP_TBL_CONTRATO WHERE CLAVE_CONTRATO = p_clave_contrato;

    -- 4. Kardex
    INSERT INTO UGTP_TBL_KARDEX (
        FECHA, ID_CONTRATO,
        ID_NODO_RECEPCION, ID_NODO_ENTREGA,
        ID_ZONA_INYECCION, ID_ZONA_EXTRACCION,
        QTY_NOM_RECEPCION, QTY_ASIG_RECEPCION,
        QTY_NOM_ENTREGA,   QTY_ASIG_ENTREGA,
        GAS_EXCESO, TARIFA_EXCESO_FIRME,
        TARIFA_USO_INTERRUMPIBLE,
        CARGO_USO, CARGO_GAS_EXCESO, TOTAL_FACTURAR
    ) VALUES (
        p_fecha, v_id_contrato,
        v_id_nodo_rec, v_id_nodo_ent,
        v_id_zona_iny, v_id_zona_ext,
        p_qty_nom_rec, p_qty_asig_rec,
        p_qty_nom_ent, p_qty_asig_ent,
        p_gas_exceso, p_tarifa_exc_firme,
        p_tarifa_uso_int,
        p_cargo_uso, p_cargo_gas_exc, p_total_facturar
    );

    COMMIT;
END UGTP_SP_CARGA_FILA;


CREATE OR REPLACE PROCEDURE UGTP_SP_CARGA_FILA (
    p_fecha                   IN DATE,
    p_clave_contrato          IN VARCHAR2,
    p_nombre_usuario          IN VARCHAR2,
    p_clave_nodo_rec          IN VARCHAR2,
    p_desc_nodo_rec           IN VARCHAR2,
    p_clave_nodo_ent          IN VARCHAR2,
    p_desc_nodo_ent           IN VARCHAR2,
    p_clave_zona_iny          IN VARCHAR2,
    p_clave_zona_ext          IN VARCHAR2,
    p_qty_nom_rec             IN NUMBER,
    p_qty_asig_rec            IN NUMBER,
    p_qty_nom_ent             IN NUMBER,
p_qty_asig_ent            IN NUMBER, 
p_gas_exceso              IN NUMBER,
    p_tarifa_exc_firme        IN NUMBER,
    p_tarifa_uso_int          IN NUMBER,
    p_cargo_uso               IN NUMBER,
    p_cargo_gas_exc           IN NUMBER,
    p_total_facturar          IN NUMBER
) AS
    v_id_usuario     UGTP_TBL_USUARIO.ID_USUARIO%TYPE;
    v_id_contrato    UGTP_TBL_CONTRATO.ID_CONTRATO%TYPE;
    v_id_nodo_rec    UGTP_TBL_NODO_COMERCIAL.ID_NODO%TYPE;
    v_id_nodo_ent    UGTP_TBL_NODO_COMERCIAL.ID_NODO%TYPE;
    v_id_zona_iny    UGTP_TBL_ZONA_TARIFA.ID_ZONA%TYPE;
    v_id_zona_ext    UGTP_TBL_ZONA_TARIFA.ID_ZONA%TYPE;
BEGIN

    -- ? CATÁLOGOS (idealmente ya con MERGE en sus SP)
    UGTP_SP_CARGA_USUARIO(p_nombre_usuario);
    UGTP_SP_CARGA_NODO(p_clave_nodo_rec, p_desc_nodo_rec);
    UGTP_SP_CARGA_NODO(p_clave_nodo_ent, p_desc_nodo_ent);
    UGTP_SP_CARGA_ZONA(p_clave_zona_iny);
    UGTP_SP_CARGA_ZONA(p_clave_zona_ext);

    -- ? IDS
    SELECT ID_USUARIO INTO v_id_usuario
    FROM UGTP_TBL_USUARIO
    WHERE NOMBRE = p_nombre_usuario
    FETCH FIRST 1 ROWS ONLY;

    SELECT ID_NODO INTO v_id_nodo_rec
    FROM UGTP_TBL_NODO_COMERCIAL
    WHERE CLAVE_NODO = p_clave_nodo_rec
    FETCH FIRST 1 ROWS ONLY;

    SELECT ID_NODO INTO v_id_nodo_ent
    FROM UGTP_TBL_NODO_COMERCIAL
    WHERE CLAVE_NODO = p_clave_nodo_ent
    FETCH FIRST 1 ROWS ONLY;

    SELECT ID_ZONA INTO v_id_zona_iny
    FROM UGTP_TBL_ZONA_TARIFA
    WHERE CLAVE_ZONA = p_clave_zona_iny
    FETCH FIRST 1 ROWS ONLY;

    SELECT ID_ZONA INTO v_id_zona_ext
    FROM UGTP_TBL_ZONA_TARIFA
    WHERE CLAVE_ZONA = p_clave_zona_ext
    FETCH FIRST 1 ROWS ONLY;

    -- ? CONTRATO
    MERGE INTO UGTP_TBL_CONTRATO c
    USING (SELECT p_clave_contrato AS CLAVE FROM DUAL) src
    ON (c.CLAVE_CONTRATO = src.CLAVE)
    WHEN NOT MATCHED THEN
        INSERT (CLAVE_CONTRATO, ID_USUARIO)
        VALUES (p_clave_contrato, v_id_usuario);

    SELECT ID_CONTRATO INTO v_id_contrato
    FROM UGTP_TBL_CONTRATO
    WHERE CLAVE_CONTRATO = p_clave_contrato
    FETCH FIRST 1 ROWS ONLY;

    -- ? KARDEX (MERGE para evitar duplicados)
    MERGE INTO UGTP_TBL_KARDEX k
    USING (
        SELECT
            p_fecha AS FECHA,
            v_id_contrato AS ID_CONTRATO,
            v_id_nodo_rec AS ID_NODO_REC,
            v_id_nodo_ent AS ID_NODO_ENT,
            v_id_zona_iny AS ID_ZONA_INY,
            v_id_zona_ext AS ID_ZONA_EXT
        FROM dual
    ) src
    ON (
        k.FECHA = src.FECHA AND
        k.ID_CONTRATO = src.ID_CONTRATO AND
        k.ID_NODO_RECEPCION = src.ID_NODO_REC AND
        k.ID_NODO_ENTREGA = src.ID_NODO_ENT AND
        k.ID_ZONA_INYECCION = src.ID_ZONA_INY AND
        k.ID_ZONA_EXTRACCION = src.ID_ZONA_EXT
    )
    WHEN MATCHED THEN
        UPDATE SET
            k.QTY_NOM_RECEPCION = p_qty_nom_rec,
            k.QTY_ASIG_RECEPCION = p_qty_asig_rec,
            k.QTY_NOM_ENTREGA = p_qty_nom_ent,
            k.GAS_EXCESO = p_gas_exceso,
            k.TARIFA_EXCESO_FIRME = p_tarifa_exc_firme,
            k.TARIFA_USO_INTERRRUMPIBLE = p_tarifa_uso_int,
            k.CARGO_USO = p_cargo_uso,
            k.CARGO_GAS_EXCESO = p_cargo_gas_exc,
            k.TOTAL_FACTURAR = p_total_facturar

    WHEN NOT MATCHED THEN
        INSERT (
            FECHA, ID_CONTRATO,
            ID_NODO_RECEPCION, ID_NODO_ENTREGA,
            ID_ZONA_INYECCION, ID_ZONA_EXTRACCION,
            QTY_NOM_RECEPCION, QTY_ASIG_RECEPCION,
            QTY_NOM_ENTREGA,
            GAS_EXCESO,
            TARIFA_EXCESO_FIRME,
            TARIFA_USO_INTERRRUMPIBLE,
            CARGO_USO, CARGO_GAS_EXCESO, TOTAL_FACTURAR
        )
        VALUES (
            p_fecha, v_id_contrato,
            v_id_nodo_rec, v_id_nodo_ent,
            v_id_zona_iny, v_id_zona_ext,
            p_qty_nom_rec, p_qty_asig_rec,
            p_qty_nom_ent,
            p_gas_exceso,
            p_tarifa_exc_firme,
            p_tarifa_uso_int,
            p_cargo_uso, p_cargo_gas_exc, p_total_facturar
        );

EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END UGTP_SP_CARGA_FILA;




CREATE TABLE UGTP_TBL_STAGING (
    FECHA                     VARCHAR2(20),
    CONTRATO                  VARCHAR2(50),
    USUARIO                   VARCHAR2(200),
    CLAVE_NODO_REC            VARCHAR2(20),
    DESC_NODO_REC             VARCHAR2(100),
    CLAVE_NODO_ENT            VARCHAR2(20),
    DESC_NODO_ENT             VARCHAR2(100),
    ZONA_INYECCION            VARCHAR2(20),
    ZONA_EXTRACCION           VARCHAR2(20),
    QTY_NOM_REC               VARCHAR2(30),
    QTY_ASIG_REC              VARCHAR2(30),
    QTY_NOM_ENT               VARCHAR2(30),
    QTY_ASIG_ENT              VARCHAR2(30),
    GAS_EXCESO                VARCHAR2(30),
    TARIFA_EXC_FIRME          VARCHAR2(30),
    TARIFA_USO_INT            VARCHAR2(30),
    CARGO_USO                 VARCHAR2(30),
    CARGO_GAS_EXC             VARCHAR2(30),
    TOTAL_FACTURAR            VARCHAR2(30)
);


CREATE OR REPLACE PROCEDURE UGTP_SP_MIGRAR_STAGING AS
BEGIN
    FOR r IN (SELECT * FROM UGTP_TBL_STAGING) LOOP
        UGTP_SP_CARGA_FILA(
            TO_DATE(r.FECHA, 'MM/DD/YYYY'),
            r.CONTRATO,
            r.USUARIO,
            r.CLAVE_NODO_REC,
            r.DESC_NODO_REC,
            r.CLAVE_NODO_ENT,
            r.DESC_NODO_ENT,
            r.ZONA_INYECCION,
            r.ZONA_EXTRACCION,
            TO_NUMBER(r.QTY_NOM_REC),
            TO_NUMBER(r.QTY_ASIG_REC),
            TO_NUMBER(r.QTY_NOM_ENT),
            TO_NUMBER(r.QTY_ASIG_ENT),
            TO_NUMBER(r.GAS_EXCESO),
            TO_NUMBER(r.TARIFA_EXC_FIRME),
            TO_NUMBER(r.TARIFA_USO_INT),
            TO_NUMBER(r.CARGO_USO),
            TO_NUMBER(r.CARGO_GAS_EXC),
            TO_NUMBER(r.TOTAL_FACTURAR)
        );
    END LOOP;
    COMMIT;
END UGTP_SP_MIGRAR_STAGING;
/

-- Ejecutar la migración:
BEGIN
    UGTP_SP_MIGRAR_STAGING;
END;
/

DROP PROCEDURE UGTP_SP_CONTRATOS_POR_USUARIO;



-- Q1: Contratos del usuario dado

CREATE OR REPLACE PROCEDURE UGTP_SP_CONTRATOS_POR_USUARIO(
p_nombre_usuario IN VARCHAR2,
p_cursor OUT SYS_REFCURSOR
)AS
BEGIN
    OPEN p_cursor FOR
    SELECT c.clave_contrato
    FROM UGTP_TBL_CONTRATO c
    JOIN UGTP_TBL_USUARIO u ON u.id_usuario = c.id_usuario
    WHERE UPPER(u.nombre) = UPPER(p_nombre_usuario);
    END UGTP_SP_CONTRATOS_POR_USUARIO;
    
    

-- Q2: Usuario asociado a un contrato

CREATE OR REPLACE PROCEDURE UGTP_SP_USUARIO_POR_CONTRATO(
    p_clave_contrato IN VARCHAR2,
    p_cursor OUT SYS_REFCURSOR
)AS
BEGIN
    OPEN p_cursor FOR
    SELECT u.nombre
    FROM UGTP_TBL_CONTRATO c
    JOIN UGTP_TBL_USUARIO u ON u.id_usuario = c.id_usuario
    WHERE UPPER(c.clave_contrato) = UPPER(p_clave_contrato);
    END UGTP_SP_USUARIO_POR_CONTRATO;
        


-- Q3: Toda la info de usuarios con nodo de recepción dado
CREATE OR REPlACE PROCEDURE UGTP_SP_INFO_NODO_RECEPCION(
p_clave_nodo IN VARCHAR2,
p_cursor OUT SYS_REFCURSOR
)AS
    BEGIN
        OPEN p_cursor FOR
    SELECT
    k.fecha,
    c.clave_contrato,
    u.nombre      as usuario,
    nr.clave_nodo as nodo_recepcion,
    nr.descripcion as desc_nodo_recepcion,
    ne.clave_nodo as nodo_entrega,
    ne.descripcion as desc_nodo_entrega,
    zi.clave_zona as zona_inyeccion,
    ze.clave_zona as zona_extraccion,
    k.qty_nom_recepcion,
    k.qty_asig_recepcion,
    k.qty_nom_entrega,
    k.qty_asig_entrega,
    k.gas_exceso,
    k.tarifa_exceso_firme,
    k.TARIFA_USO_INTERRRUMPIBLE,
    k.cargo_uso,
    k.cargo_gas_exceso,
    k.total_facturar
    FROM UGTP_TBL_KARDEX k
    JOIN UGTP_TBL_CONTRATO c ON c.id_contrato = k.id_contrato
    JOIN UGTP_TBL_USUARIO u ON u.id_usuario = c.id_usuario
    JOIN UGTP_TBL_NODO_COMERCIAL nr ON nr.id_nodo = k.id_nodo_recepcion
    JOIN UGTP_TBL_NODO_COMERCIAL ne ON ne.id_nodo = k.id_nodo_entrega
    JOIN UGTP_TBL_ZONA_TARIFA zi ON zi.id_zona = k.id_zona_inyeccion
    JOIN UGTP_TBL_ZONA_TARIFA ze ON ze.id_zona = k.id_zona_extraccion
    WHERE UPPER(nr.clave_nodo) = UPPER(p_clave_nodo);
    END UGTP_SP_INFO_NODO_RECEPCION;
    
    -- Q4: Contrato, usuario, nodos de recepción y entrega — filtrado por nodos de entrega

    CREATE OR REPLACE PROCEDURE UGTP_SP_INFO_NODOS_ENTREGA(
        p_clave1 IN VARCHAR2,
        p_clave2 IN VARCHAR2,
        p_clave3 IN VARCHAR2,
        p_cursor OUT SYS_REFCURSOR
    )AS
    BEGIN
        OPEN p_cursor FOR
        SELECT
        c.clave_contrato,
        u.nombre as usaurio,
        nr.clave_nodo as nodo_recepcion,
        nr.descripcion as desc_nodo_recepcion,
        ne.clave_nodo as nodo_entrega,
        ne.descripcion as desc_nodo_entrega
        FROM UGTP_TBL_KARDEX k
        JOIN UGTP_TBL_CONTRATO c ON c.id_contrato = k.id_contrato
        JOIN UGTP_TBL_USUARIO u ON u.id_usuario = c.id_usuario
        JOIN UGTP_TBL_NODO_COMERCIAL nr ON nr.id_nodo = k.id_nodo_recepcion
        JOIN UGTP_TBL_NODO_COMERCIAL ne ON ne.id_nodo = k.id_nodo_entrega
        WHERE UPPER(ne.clave_nodo)IN (UPPER(p_clave1), UPPER(p_clave2), UPPER(p_clave3));
        END UGTP_SP_INFO_NODOS_ENTREGA;
        
 -- Q5: Todos los nodos de recepción únicos
 CREATE OR REPLACE PROCEDURE UGTP_SP_NODOS_RECEPCION(
 p_cursor OUT SYS_REFCURSOR
 )AS      
    BEGIN
        OPEN p_cursor FOR
        SELECT DISTINCT
                nr.clave_nodo,
                nr.descripcion
                FROM UGTP_TBL_KARDEX k
                JOIN UGTP_TBL_NODO_COMERCIAL nr ON nr.id_nodo = k.id_nodo_recepcion
                ORDER BY nr.clave_nodo;
                END UGTP_SP_NODOS_RECEPCION;
        
        
        -- Q6: Todos los nodos de entrega únicos

CREATE OR REPLACE PROCEDURE UGTP_SP_NODOS_ENTREGA(
    p_cursor OUT SYS_REFCURSOR
)AS
    BEGIN
        OPEN p_cursor FOR
        SELECT DISTINCT
            ne.clave_nodo,
            ne.descripcion
    FROM UGTP_TBL_KARDEX k
    JOIN UGTP_TBL_NODO_COMERCIAL ne ON ne.id_nodo = k.id_nodo_entrega
    ORDER BY ne.clave_nodo;
    END UGTP_SP_NODOS_ENTREGA;
    
-- Q7: Toda la info de usuarios con zona de inyección dada

CREATE OR REPLACE PROCEDURE UGTP_SP_INFO_ZONA_INYECCION(
p_clave_zona IN VARCHAR2,
p_cursor OUT SYS_REFCURSOR
)AS 
    BEGIN
        OPEN p_cursor FOR
        SELECT
                k.fecha,
                c.clave_contrato,
                u.nombre as usuario,
                nr.clave_nodo as nodo_recepcion,
                nr.descripcion as desc_nodo_recepcion,
                ne.clave_nodo   as nodo_entrega,
                ne.descripcion as desc_nodo_entrega,
                zi.clave_zona as zona_inyeccion,
                ze.clave_zona as zona_extraccion,
                k.qty_nom_recepcion,
                k.qty_asig_recepcion,
                k.qty_nom_entrega,
                k.qty_asig_entrega,
                k.gas_exceso,
                k.tarifa_exceso_firme,
                k.TARIFA_USO_INTERRRUMPIBLE,
                k.cargo_uso,
                k.cargo_gas_exceso,
                k.total_facturar
                FROM UGTP_TBL_KARDEX k
                JOIN UGTP_TBL_CONTRATO c ON c.id_contrato = k.id_contrato
                JOIN UGTP_TBL_USUARIO u ON u.id_usuario = c.id_usuario
                JOIN UGTP_TBL_NODO_COMERCIAL nr ON nr.id_nodo = k.id_nodo_recepcion
                JOIN UGTP_TBL_NODO_COMERCIAL ne ON ne.id_nodo = k.id_nodo_entrega
                JOIN UGTP_TBL_ZONA_TARIFA zi ON zi.id_zona = k.id_zona_inyeccion
                JOIN UGTP_TBL_ZONA_TARIFA ze ON ze.id_zona = k.id_zona_extraccion
                WHERE UPPER(zi.clave_zona) = UPPER(p_clave_zona);
                END UGTP_SP_INFO_ZONA_INYECCION;
                
-- Q8: Toda la info de usuarios con zonas de extracción dadas (2, 5 y 6)
                
CREATE OR REPLACE PROCEDURE UGTP_SP_INFO_ZONAS_EXTRACCION(
p_zona1 IN VARCHAR2,
p_zona2 IN VARCHAR2,
p_zona3 IN VARCHAR2,
p_cursor OUT SYS_REFCURSOR
)AS
    BEGIN
        OPEN p_cursor FOR
                SElECT
                    k.fecha,
                    c.clave_contrato,
                    u.nombre as usuario,
                    nr.clave_nodo as nodo_recepcion,
                    nr.descripcion as desc_nodo_recepcion,
                    ne.clave_nodo as nodo_entrega,
                    ne.descripcion as desc_nodo_entrega,
                    zi.clave_zona as zona_inyeccion,
                    ze.clave_zona as zona_extraccion,
                    k.qty_nom_recepcion,
                    k.qty_asig_recepcion,
                    k.qty_nom_entrega,
                    k.qty_asig_entrega,
                    k.gas_exceso,
                    k.tarifa_exceso_firme,
                    k.tarifa_uso_interrrumpible,
                    k.cargo_uso,
                    k.cargo_gas_exceso,
                    k.total_facturar
                    FROM UGTP_TBL_KARDEX k
                    JOIN UGTP_TBL_CONTRATO c  ON c.id_contrato = k.id_contrato
                    JOIN UGTP_TBL_USUARIO u ON  u.id_usuario = c.id_usuario
                    JOIN UGTP_TBL_NODO_COMERCIAL nr ON nr.id_nodo = k.id_nodo_recepcion
                    JOIN UGTP_TBL_NODO_COMERCIAL ne ON ne.id_nodo = k.id_nodo_entrega
                    JOIN UGTP_TBL_ZONA_TARIFA zi ON zi.id_zona = k.id_zona_inyeccion
                    JOIN UGTP_TBL_ZONA_TARIFA ze ON ze.id_zona = k.id_zona_extraccion
                    WHERE UPPER(ze.clave_zona) IN (UPPER(p_zona1), UPPER(p_zona2), UPPER(p_zona3)
                    );
                    END UGTP_SP_INFO_ZONAS_EXTRACCION;
        
-- Q9: Todos los datos de usuarios de contratos específicos
    CREATE OR REPLACE PROCEDURE UGTP_SP_INFO_CONTRATOS(
    p_contrato1 IN VARCHAR2,
    p_contrato2 IN VARCHAR2,
    p_contrato3 IN VARCHAR2,
    p_contrato4 IN VARCHAR2,
    p_cursor OUT SYS_REFCURSOR
    )AS
       BEGIN
            OPEN p_cursor FOR
                SELECT
               k.fecha,
                    c.clave_contrato,
                    u.nombre as usuario,
                    nr.clave_nodo as nodo_recepcion,
                    nr.descripcion as desc_nodo_recepcion,
                    ne.clave_nodo as nodo_entrega,
                    ne.descripcion as desc_nodo_entrega,
                    zi.clave_zona as zona_inyeccion,
                    ze.clave_zona as zona_extraccion,
                    k.qty_nom_recepcion,
                    k.qty_asig_recepcion,
                    k.qty_nom_entrega,
                    k.qty_asig_entrega,
                    k.gas_exceso,
                    k.tarifa_exceso_firme,
                    k.tarifa_uso_interrrumpible,
                    k.cargo_uso,
                    k.cargo_gas_exceso,
                    k.total_facturar
                    FROM UGTP_TBL_KARDEX k
                    JOIN UGTP_TBL_CONTRATO c  ON c.id_contrato = k.id_contrato
                    JOIN UGTP_TBL_USUARIO u ON  u.id_usuario = c.id_usuario
                    JOIN UGTP_TBL_NODO_COMERCIAL nr ON nr.id_nodo = k.id_nodo_recepcion
                    JOIN UGTP_TBL_NODO_COMERCIAL ne ON ne.id_nodo = k.id_nodo_entrega
                    JOIN UGTP_TBL_ZONA_TARIFA zi ON zi.id_zona = k.id_zona_inyeccion
                    JOIN UGTP_TBL_ZONA_TARIFA ze ON ze.id_zona = k.id_zona_extraccion
               WHERE UPPER(c.CLAVE_CONTRATO) IN (
                UPPER(p_contrato1), UPPER(p_contrato2),UPPER(p_contrato3),UPPER(p_contrato4)
               );
               
               END UGTP_SP_INFO_CONTRATOS;
               
               CREATE OR REPLACE PROCEDURE UGTP_SP_TOTAL_FACTURAR_USUARIO(
               p_nombre_usuario IN VARCHAR2,
               p_anio IN NUMBER,
               p_mes    IN NUMBER,
               p_total OUT NUMBER
               )AS
                  BEGIN 
                        SELECT NVL(SUM(k.TOTAL_FACTURAR),0)
                        INTO p_total
                        FROM UGTP_TBL_KARDEX k
                        JOIN UGTP_TBL_CONTRATO c ON c.id_contrato = k.id_contrato
                        JOIN UGTP_TBL_USUARIO u ON u.id_usuario = c.id_usuario
                        WHERE UPPER(u.nombre) = UPPER(p_nombre_usuario)
                        AND EXTRACT(YEAR FROM k.FECHA) = p_anio
                        AND EXTRACT(MONTH FROM k.FECHA) = p_mes;
                        END UGTP_SP_TOTAL_FACTURAR_USUARIO;
                        
                        
                        CREATE OR REPLACE PROCEDURE UGTP_SP_PROMEDIO_NOM_RECEPCION(
                        p_nombre_usuario IN VARCHAR2,
                        p_anio IN NUMBER,
                        p_mes IN NUMBER,
                        p_promedio OUT NUMBER
                        )AS 
                            BEGIN
                                SELECT NVL(AVG(k.qty_nom_recepcion),0)
                                INTO p_promedio
                                FROM UGTP_TBL_KARDEX k
                                JOIN UGTP_TBL_CONTRATO c ON c.id_contrato = k.id_contrato
                                JOIN UGTP_TBL_USUARIO u ON u.id_usuario = u.id_usuario
                                WHERE UPPER(u.nombre) = UPPER(p_nombre_usuario)
                                AND EXTRACT(YEAR FROM k.fecha) = p_anio
                                AND EXTRACT (MONTH FROM k.fecha) = p_mes;
                                END UGTP_SP_PROMEDIO_NOM_RECEPCION;
                                


 CREATE OR REPLACE PROCEDURE UGTP_SP_CARGA_CONTRATO(
 p_clave_contrato IN VARCHAR2,
 p_nombre_usuario IN VARCHAR2
 )AS
    v_id_usuario UGTP_TBL_USUARIO.ID_USUARIO%TYPE;
    BEGIN
        SELECT ID_USUARIO INTO v_id_usuario
        FROM UGTP_TBL_USUARIO
        WHERE UPPER(NOMBRE) = UPPER(p_nombre_usuario);
        
        MERGE INTO UGTP_TBL_CONTRATO c
        USING (SELECT p_clave_contrato AS CLAVE FROM DUAL) src
        ON (UPPER(c.CLAVE_CONTRATO) = UPPER(src.CLAVE))
        WHEN NOT MATCHED THEN
        INSERT (CLAVE_CONTRATO, ID_USUARIO)
        VALUES (p_clave_contrato, v_id_usuario);
        END UGTP_SP_CARGA_CONTRATO;
                    
                    
    
    CREATE OR REPLACE PROCEDURE UGTP_SP_CARGA_KARDEX (
    p_fecha            IN DATE,
    p_id_contrato      IN NUMBER,
    p_id_nodo_rec      IN NUMBER,
    p_id_nodo_ent      IN NUMBER,
    p_id_zona_iny      IN NUMBER,
    p_id_zona_ext      IN NUMBER,
    p_qty_nom_rec      IN NUMBER,
    p_qty_asig_rec     IN NUMBER,
    p_qty_nom_ent      IN NUMBER,
    p_qty_asig_ent     IN NUMBER,
    p_gas_exceso       IN NUMBER,
    p_tarifa_exc_firme IN NUMBER,
    p_tarifa_uso_int   IN NUMBER,
    p_cargo_uso        IN NUMBER,
    p_cargo_gas_exc    IN NUMBER,
    p_total_facturar   IN NUMBER
) AS
BEGIN
    MERGE INTO UGTP_TBL_KARDEX k
    USING (
        SELECT
            p_fecha       AS FECHA,
            p_id_contrato AS ID_CONTRATO,
            p_id_nodo_rec AS ID_NODO_REC,
            p_id_nodo_ent AS ID_NODO_ENT,
            p_id_zona_iny AS ID_ZONA_INY,
            p_id_zona_ext AS ID_ZONA_EXT
        FROM DUAL
    ) src
    ON (
        k.FECHA             = src.FECHA        AND
        k.ID_CONTRATO       = src.ID_CONTRATO  AND
        k.ID_NODO_RECEPCION = src.ID_NODO_REC  AND
        k.ID_NODO_ENTREGA   = src.ID_NODO_ENT  AND
        k.ID_ZONA_INYECCION = src.ID_ZONA_INY  AND
        k.ID_ZONA_EXTRACCION= src.ID_ZONA_EXT
    )
    WHEN MATCHED THEN
        UPDATE SET
            k.QTY_NOM_RECEPCION       = p_qty_nom_rec,
            k.QTY_ASIG_RECEPCION      = p_qty_asig_rec,
            k.QTY_NOM_ENTREGA         = p_qty_nom_ent,
            k.QTY_ASIG_ENTREGA        = p_qty_asig_ent,
            k.GAS_EXCESO              = p_gas_exceso,
            k.TARIFA_EXCESO_FIRME     = p_tarifa_exc_firme,
            k.TARIFA_USO_INTERRRUMPIBLE= p_tarifa_uso_int,
            k.CARGO_USO               = p_cargo_uso,
            k.CARGO_GAS_EXCESO        = p_cargo_gas_exc,
            k.TOTAL_FACTURAR          = p_total_facturar
    WHEN NOT MATCHED THEN
        INSERT (
            FECHA, ID_CONTRATO,
            ID_NODO_RECEPCION, ID_NODO_ENTREGA,
            ID_ZONA_INYECCION, ID_ZONA_EXTRACCION,
            QTY_NOM_RECEPCION, QTY_ASIG_RECEPCION,
            QTY_NOM_ENTREGA,   QTY_ASIG_ENTREGA,
            GAS_EXCESO,
            TARIFA_EXCESO_FIRME, TARIFA_USO_INTERRRUMPIBLE,
            CARGO_USO, CARGO_GAS_EXCESO, TOTAL_FACTURAR
        ) VALUES (
            p_fecha, p_id_contrato,
            p_id_nodo_rec, p_id_nodo_ent,
            p_id_zona_iny, p_id_zona_ext,
            p_qty_nom_rec, p_qty_asig_rec,
            p_qty_nom_ent, p_qty_asig_ent,
            p_gas_exceso,
            p_tarifa_exc_firme, p_tarifa_uso_int,
            p_cargo_uso, p_cargo_gas_exc, p_total_facturar
        );
END UGTP_SP_CARGA_KARDEX;


CREATE OR REPLACE PROCEDURE UGTP_SP_GET_IDS_ZONAS (
    p_cursor OUT SYS_REFCURSOR
) AS
BEGIN
    OPEN p_cursor FOR
        SELECT ID_ZONA, CLAVE_ZONA FROM UGTP_TBL_ZONA_TARIFA;
END UGTP_SP_GET_IDS_ZONAS;


CREATE OR REPLACE PROCEDURE UGTP_SP_GET_IDS_NODOS (
    p_cursor OUT SYS_REFCURSOR
) AS
BEGIN
    OPEN p_cursor FOR
        SELECT ID_NODO, CLAVE_NODO FROM UGTP_TBL_NODO_COMERCIAL;
END UGTP_SP_GET_IDS_NODOS;


CREATE OR REPLACE PROCEDURE UGTP_SP_GET_IDS_CONTRATOS (
    p_cursor OUT SYS_REFCURSOR
) AS
BEGIN
    OPEN p_cursor FOR
        SELECT ID_CONTRATO, CLAVE_CONTRATO FROM UGTP_TBL_CONTRATO;
END UGTP_SP_GET_IDS_CONTRATOS;


TRUNCATE TABLE UGTP_TBL_KARDEX;
TRUNCATE TABLE UGTP_TBL_CONTRATO;
TRUNCATE TABLE UGTP_TBL_NODO_COMERCIAL;
TRUNCATE TABLE UGTP_TBL_STAGING;
TRUNCATE TABLE UGTP_TBL_USUARIO;
TRUNCATE TABLE UGTP_TBL_ZONA_TARIFA;

DROP TABLE UGTP_TBL_STAGING;


DECLARE
    v_cursor SYS_REFCURSOR;
    v_nombre VARCHAR2(100);
BEGIN
    -- Llamada al procedimiento con el contrato que quieras probar
    UGTP_SP_USUARIO_POR_CONTRATO(p_clave_contrato => 'CENAGAS/A/500/18', 
                                 p_cursor        => v_cursor);

    -- Recorrer el cursor y mostrar resultados
    LOOP
        FETCH v_cursor INTO v_nombre;
        EXIT WHEN v_cursor%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE('Usuario: ' || v_nombre);
    END LOOP;

    CLOSE v_cursor;
END;
/DROP PROCEDURE UGTP_SP_CONTRATOS_POR_USUARIO;


DROP PROCEDURE UGTP_SP_USUARIO_POR_CONTRATO;


create or replace NONEDITIONABLE PROCEDURE UGTP_SP_USUARIO_POR_CONTRATO(
p_nombre_usuario IN VARCHAR2,
p_cursor OUT SYS_REFCURSOR
)AS
BEGIN
    OPEN p_cursor FOR
    SELECT c.clave_contrato
    FROM UGTP_TBL_CONTRATO c
    JOIN UGTP_TBL_USUARIO u ON u.id_usuario = c.id_usuario
    WHERE UPPER(u.nombre) = UPPER(p_nombre_usuario);
    END UGTP_SP_CONTRATOS_POR_USUARIO;


create or replace NONEDITIONABLE PROCEDURE UGTP_SP_CONTRATO_POR_USUARIO(
    p_clave_contrato IN VARCHAR2,
    p_cursor OUT SYS_REFCURSOR
)AS
BEGIN
    OPEN p_cursor FOR
    SELECT u.nombre
    FROM UGTP_TBL_CONTRATO c
    JOIN UGTP_TBL_USUARIO u ON u.id_usuario = c.id_usuario
    WHERE UPPER(c.clave_contrato) = UPPER(p_clave_contrato);
    END UGTP_SP_USUARIO_POR_CONTRATO;



