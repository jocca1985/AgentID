SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

# Load environment variables from .env file
set -a
source ".env"
set +a

java -jar target/aligned-execution-engine-0.0.1-SNAPSHOT.jar 1>&2