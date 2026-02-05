CREATE TABLE IF NOT EXISTS madplanen.ingrediens_type (
    kode TEXT PRIMARY KEY,
    navn TEXT NOT NULL
);

INSERT INTO madplanen.ingrediens_type (kode, navn) VALUES
    ('MEAT', 'Kød og fisk'),
    ('FRUIT', 'Frugt'),
    ('VEGETABLE', 'Grøntsag'),
    ('UNDEFINED', 'Andet')
ON CONFLICT (kode) DO NOTHING;