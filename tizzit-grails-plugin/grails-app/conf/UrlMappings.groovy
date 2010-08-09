class UrlMappings {
	static excludes = ["/images/*", "/css/*", "/js/*", "/WEB-INF/*", "*/images/*", "*/css/*", "*/js/*"]

	static mappings = {
		"/$tizzituri**" (controller: "tizzit")

		"/$controller/$action?/$id?" {
			constraints {
				// apply constraints here
			}
		}

		"/"(controller: "tizzit")
		"500"(view: '/error')
	}
}
