#!/usr/bin/env sh
set -eu

BACKUP_DIR="${DB_BACKUP_DIR:-./backups}"
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"
DB_NAME="${DB_NAME:-hospital_db}"
DB_USER="${DB_USER:-hospital_user}"
TS="$(date +"%Y%m%d-%H%M%S")"

mkdir -p "$BACKUP_DIR"
OUTPUT="$BACKUP_DIR/hospital-backup-$TS.sql"

if command -v pg_dump >/dev/null 2>&1; then
  PGPASSWORD="${DB_PASSWORD:-}" pg_dump -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -f "$OUTPUT"
  echo "Backup created at $OUTPUT"
else
  echo "pg_dump is not installed; install PostgreSQL client tools." >&2
  exit 1
fi
