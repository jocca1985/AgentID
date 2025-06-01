import { Hono } from 'hono';
import { cors } from 'hono/cors';
import { serveStatic } from '@hono/node-server/serve-static';
import * as accountController from './controller/accountController';
import { authMiddleware } from './middleware/authMiddleware';
import path from 'path';
import fs from 'fs';


// Create Hono app
const app = new Hono();

// Configure middleware
app.use('*', cors({
    origin: '*',
    allowHeaders: ['Content-Type', 'Authorization'],
    allowMethods: ['GET', 'POST', 'OPTIONS'],
    maxAge: 86400,
}));


// API routes
app.get('/api/balance', accountController.getBalance);
app.post('/api/deposit', authMiddleware, accountController.deposit);
app.post('/api/withdraw', authMiddleware, accountController.withdraw);

// Health check endpoint
app.get('/health', (c) => {
    return c.json({
        status: 'ok',
        message: 'Acme Bank API is running',
        timestamp: new Date().toISOString()
    });
});

app.get('/', async (c) => {
    try {
        const publicPath = path.join(__dirname, 'public');
        const indexPath = path.join(publicPath, 'index.html');

        // Check if file exists
        if (fs.existsSync(indexPath)) {
            const content = fs.readFileSync(indexPath, 'utf-8');
            return c.html(content);
        } else {
            console.error(`File not found: ${indexPath}`);
            return c.text('Dashboard not found. Please check server configuration.', 404);
        }
    } catch (error) {
        console.error('Error serving index.html:', error);
        return c.text('Error serving dashboard', 500);
    }
});


const getPublicPath = () => {
    // Check if we're in development or production
    const isDev = process.env.NODE_ENV !== 'production';
    console.log('Environment:', isDev ? 'Development' : 'Production');

    if (isDev) {
        // In development, public is at project root
        return path.join(process.cwd(), 'public');
    } else {
        // In production (after build), public is copied to dist/public
        // First check if dist/public exists
        const distPublicPath = path.join(process.cwd(), 'dist', 'public');
        if (fs.existsSync(distPublicPath)) {
            return distPublicPath;
        }

        // Fallback to public at project root
        return path.join(process.cwd(), 'public');
    }
};

// Serve static files - give higher priority to these specific files
app.get('/styles.css', (c) => {
    const publicPath = getPublicPath();
    const filePath = path.join(publicPath, 'styles.css');
    if (fs.existsSync(filePath)) {
        const content = fs.readFileSync(filePath, 'utf-8');
        return c.text(content, 200, {
            'Content-Type': 'text/css'
        });
    }
    return c.text('File not found', 404);
});

app.get('/app.js', (c) => {
    const publicPath = getPublicPath();
    const filePath = path.join(publicPath, 'app.js');
    if (fs.existsSync(filePath)) {
        const content = fs.readFileSync(filePath, 'utf-8');
        return c.text(content, 200, {
            'Content-Type': 'application/javascript'
        });
    }
    return c.text('File not found', 404);
});

// Root route - serve index.html
app.get('/', (c) => {
    const publicPath = getPublicPath();
    const filePath = path.join(publicPath, 'index.html');

    console.log('Looking for index.html at:', filePath);

    if (fs.existsSync(filePath)) {
        const content = fs.readFileSync(filePath, 'utf-8');
        return c.html(content);
    }

    return c.text('Dashboard not found', 404);
});

// Catch-all route for other static files
app.get('/*', (c) => {
    const publicPath = getPublicPath();
    const requestPath = c.req.path;

    // Avoid handling API requests
    if (requestPath.startsWith('/api/')) {
        return c.notFound();
    }

    const filePath = path.join(publicPath, requestPath);

    console.log('Looking for file at:', filePath);

    if (fs.existsSync(filePath)) {
        try {
            const content = fs.readFileSync(filePath);

            // Determine content type
            let contentType = 'text/plain';
            if (requestPath.endsWith('.css')) contentType = 'text/css';
            else if (requestPath.endsWith('.js')) contentType = 'application/javascript';
            else if (requestPath.endsWith('.html')) contentType = 'text/html';
            else if (requestPath.endsWith('.json')) contentType = 'application/json';
            else if (requestPath.endsWith('.png')) contentType = 'image/png';
            else if (requestPath.endsWith('.jpg') || requestPath.endsWith('.jpeg')) contentType = 'image/jpeg';

            return new Response(content, {
                headers: {
                    'Content-Type': contentType
                }
            });
        } catch (error) {
            console.error('Error reading file:', error);
            return c.text('Error reading file', 500);
        }
    }

    return c.text('File not found', 404);
});

export default app;