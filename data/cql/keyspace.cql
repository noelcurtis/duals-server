CREATE KEYSPACE IF NOT EXISTS "test_clash"
WITH REPLICATION = { 'class': 'SimpleStrategy', 'replication_factor': 3 };