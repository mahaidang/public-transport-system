const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
  app.use(
    '/nominatim',
    createProxyMiddleware({
      target: 'https://nominatim.openstreetmap.org',
      changeOrigin: true,
      pathRewrite: { '^/nominatim': '' }
    })
  );
};
