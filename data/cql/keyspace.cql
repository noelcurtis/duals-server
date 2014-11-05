CREATE KEYSPACE IF NOT EXISTS "clash"
WITH REPLICATION = { 'class': 'SimpleStrategy', 'replication_factor': 3 };