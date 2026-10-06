# Patch notes

I fixed the search predicate so archived tasks cannot reappear and the selected status applies to title and description matches. I kept the H2 query, reference SQL, and Oracle artifact consistent. I also removed intentional request sleeps, validate pagination and status inputs as 400 responses, and apply pagination in the database rather than loading every match first. Integration tests cover the corrected filtering and validation behaviour.

On the UI, changing a search or status filter resets pagination. Text search is debounced, and obsolete requests are aborted so a slower response cannot overwrite newer results; loading and error state now also recover correctly.

I did not add task creation, authentication, sorting, or schema indexes because they exceed this focused read-only tracker patch. The principal remaining risk is that broad `%term%` search will not scale well on a large data set; a production system would use an indexed full-text search strategy.

I used Codex to inspect the codebase, reproduce API responses locally, draft the patch and tests, then ran the application and build checks myself.
