package main

import (
	"log"
	"net/http"
	"net/http/httputil"
	"net/url"
	"strings"
)

// Service routes
var serviceMap = map[string]string{
	"bikes":        "http://bike-manager:8080",    // bike-manager
	"users":        "http://account-manager:8080", // account-manager
	"ride-service": "http://ride-manager:8080",    // ride-manager
	"auth":         "http://auth-service:8080",    // auth-service
}

// forwards the request to the appropriate service
func ProxyRequest(w http.ResponseWriter, r *http.Request) {
	// extract URL path
	parts := strings.Split(strings.Trim(r.URL.Path, "/"), "/")
	if len(parts) < 1 {
		http.Error(w, "Bad request", http.StatusBadRequest)
		return
	}
	serviceName := parts[0]

	// look up the service URL
	serviceURL, ok := serviceMap[serviceName]
	if !ok {
		http.Error(w, "Service not found", http.StatusNotFound)
		return
	}

	// target URL
	targetURL, err := url.Parse(serviceURL)
	if err != nil {
		http.Error(w, "Invalid service URL", http.StatusInternalServerError)
		return
	}

	// Forward the request
	proxy := httputil.NewSingleHostReverseProxy(targetURL)
	r.URL.Path = "/" + strings.Join(parts, "/") // Rewrite the URL path
	r.Host = targetURL.Host                     // Update the Host header

	log.Printf("Forwarding request: %s -> %s%s", r.URL.Path, targetURL, r.URL.Path)

	proxy.ErrorHandler = func(w http.ResponseWriter, req *http.Request, e error) {
		log.Printf("Proxy error: %v", e)
		http.Error(w, "Proxy error", http.StatusBadGateway)
	}
	proxy.ServeHTTP(w, r)
}

func main() {
	// Log startup
	log.Println("Starting API Gateway on port 8080...")

	// Handle requests
	http.HandleFunc("/", ProxyRequest)

	// Start the server
	log.Fatal(http.ListenAndServe(":8080", nil))
}
