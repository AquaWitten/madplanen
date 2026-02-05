CREATE TABLE IF NOT EXISTS madplanen.ingrediens (
    id BIGSERIAL PRIMARY KEY,
    navn TEXT NOT NULL UNIQUE,
    beskrivelse TEXT,
    type_kode TEXT NOT NULL,

    CONSTRAINT fk_ingrediens_type
        FOREIGN KEY (type_kode)
        REFERENCES madplanen.ingrediens_type (kode)
        ON DELETE RESTRICT
);

INSERT INTO madplanen.ingrediens (navn, beskrivelse, type_kode) VALUES
    ('Æble', 'Frugt der hænger på træer', 'FRUIT')
ON CONFLICT (navn) DO NOTHING;