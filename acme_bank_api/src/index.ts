import { serve } from '@hono/node-server';
import app from './app';

// Server configuration
const PORT = process.env.PORT || 5025;

// Start the server
console.log(`Acme Bank Web API running at http://localhost:${PORT}`);
serve({
    fetch: app.fetch,
    port: Number(PORT)
});