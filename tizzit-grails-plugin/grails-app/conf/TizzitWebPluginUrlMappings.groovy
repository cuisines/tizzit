class TizzitWebPluginUrlMappings {
	static excludes = ["/images/*", "/css/*", "/js/*", "/WEB-INF/*", "*/images/*", "*/css/*", "*/js/*"]

	static mappings = {
		"/$tizzituri**" (controller: "tizzit")
		"/picture/$id" (controller: "tizzit", action: "picture")
		"/picture/$mimeType/$id" (controller: "tizzit", action: "picture")
		"/favicon*" (controller: "tizzit", action: "favicon")


		"/"(controller: "tizzit")
		"500"(view: '/error')
	}
}
