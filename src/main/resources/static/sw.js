const staticCacheName = "pwa";

self.addEventListener("install", function (e) {
    e.waitUntil(
        caches.open(staticCacheName).then(function (cache) {
            return cache.addAll(["/"]);
        })
    );
});

self.addEventListener("fetch", function (event) {
    event.respondWith(
        fetch(event.request).then(function (response) {
            return caches.open(staticCacheName).then(function (cache) {
                cache.put(event.request, response.clone()).then(r => console.log(r));
                return response;
            });
        }).catch(function () {
            return caches.match(event.request);
        })
    );
});