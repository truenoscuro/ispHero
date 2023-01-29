// Service Worker for PWA
// This service worker is a basic service worker that will serve files from the cache and
// from the network. It will cache all files that are in the static directory and the index.html
// file. It will also cache the /api/notes and /api/notes/{id} endpoints. It will not cache
// any other endpoints. It will also cache the /api/notes and /api/notes/{id} endpoints.
const cacheKey = 'ISP_HERO_CACHE';
self.addEventListener('install', (event) => {

    event.waitUntil(caches.open(cacheKey).then((cache) => {
        // Add all the assets in the array to the 'MyFancyCacheName_v1'
        // `Cache` instance for later use.
        return cache.addAll([
            '/',
            '/about',
            '/contact',
            '/login',
            '/register',
        ]);
    }));
});

self.addEventListener('fetch', (event) => {
    // Check if this is a navigation request
    if (event.request.mode === 'navigate') {
        // Open the cache
        event.respondWith(caches.open(cacheKey).then((cache) => {
            // Go to the network first
            return fetch(event.request.url).then((fetchedResponse) => {
                cache.put(event.request, fetchedResponse.clone());
                return fetchedResponse;
            }).catch(() => {
                // If the network is unavailable, get
                return cache.match(event.request.url);
            });
        }));
    }
});