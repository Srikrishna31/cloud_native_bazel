common_deps = [
    "@maven//:com_google_cloud_functions_function_maven_plugin",
    "@maven//:com_google_cloud_functions_functions_framework_api",
]

java_library(
    name = "cloud_function",
    srcs = [
        "main/java/com/example/Example.java",
    ],
    deps = common_deps,
)

common_test_deps = [
    "@maven//:org_junit_jupiter_junit_jupiter_engine",
    "@maven//:org_mockito_mockito_core",
]

java_test(
    name = "cloud_function_test",
    srcs = [
        "test/java/com/example/ExampleTest.java",
    ],
    test_class = "com.example.ExampleTest",
    deps = [
        ":cloud_function",
    ] + common_deps + common_test_deps,
)

java_test(
    name = "cloud_function_integration_test",
    srcs = [
        "test/java/com/example/ExampleIntegrationTest.java",
    ],
    test_class = "com.example.ExampleIntegrationTest",
    deps = [
        ":cloud_function",
    ] + common_deps + common_test_deps,
)

java_test(
    name = "cloud_function_system_test",
    srcs = [
        "test/java/com/example/ExampleSystemTest.java",
    ],
    test_class = "com.example.ExampleSystemTest",
    deps = [
        ":cloud_function",
    ] + common_test_deps + common_deps,
)
