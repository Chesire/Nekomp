# Sometimes it's a README fix, or something like that - which isn't relevant for
# including in a project's CHANGELOG for example
declared_trivial = github.pr_title.include? "#trivial"

# Make it more obvious that a PR is a work in progress and shouldn't be merged yet
warn("PR is classed as Work in Progress") if github.pr_title.include? "[WIP]"

# Warn when there is a big PR
warn("Big PR") if git.lines_of_code > 500

kotlin_detekt.gradle_task = "detektCheck"
kotlin_detekt.skip_gradle_task = true
kotlin_detekt.report_file = "build/reports/detekt/detekt.xml"
kotlin_detekt.detekt(inline_mode: true)

lint_dir = "**/reports/lint-results*.xml"
Dir[lint_dir].each do |file_name|
  android_lint.skip_gradle_task = true
  android_lint.report_file = file_name
  android_lint.lint(inline_mode: true)
end
