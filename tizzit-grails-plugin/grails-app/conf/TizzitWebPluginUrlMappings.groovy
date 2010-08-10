class TizzitWebPluginUrlMappings {
	static excludes = ["/images/*", "/css/*", "/js/*", "/WEB-INF/*", "*/images/*", "*/css/*", "*/js/*"]

	static mappings = {
		"/$tizzituri**" (controller: "tizzit")

		"/"(controller: "tizzit")
		"500"(view: '/error')
	}
}
