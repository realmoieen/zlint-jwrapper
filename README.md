[![Gradle Build](https://github.com/realmoieen/zlint-jwrapper/actions/workflows/gradle-build.yml/badge.svg)](https://github.com/realmoieen/zlint-jwrapper/actions/workflows/gradle-build.yml)  [![Gradle Release](https://github.com/realmoieen/zlint-jwrapper/actions/workflows/release.yml/badge.svg)](https://github.com/realmoieen/zlint-jwrapper/actions/workflows/release.yml)
# Zlint jWrapper

## Overview

The Zlint Java Wrapper is a Java-based utility that wraps around the `zlint` command-line tool. ZLint is a X.509
certificate linter written in Java that checks for consistency with standards (e.g. RFC 5280) and other relevant PKI
requirements. It uses
the `ProcessBuilder` class to execute [zlint](https://github.com/zmap/zlint) commands and capture the results. This
wrapper allows you to integrate Zlint's linting capabilities into your Java applications seamlessly.

## Features

- Execute `zlint` commands from Java.
- Intenally process the `zlint` json result and give a high level result.
- Dynamically set the working directory for `zlint` execution.
- Handle different operating systems (Windows, Linux, Mac).

## Requirements

- Java 8 or higher
- `zlint` command-line tool installed and accessible in your system's PATH
  or
- you can set the custom working directory of `zlint` only once before linting
    ```java
    ZLinter.setZlintPath("Directory_Containing_Zlint");
    ```

## Installation

1. **Clone the repository:**
   ```sh
   git clone https://github.com/realmoieen/zlint-jwrapper.git
   cd zlint-java-wrapper
    ```

2. Build the project:
   Run the follwoing gradle command to build the project
    ```shell
    gradlew jar
    ```

## Usage

### Example Code

```java
public class ZLinterExample {
    public static void main(String[] args) throws ZLintException {
        //first set the ZLint path
        ZLinter.setZlintPath("Directory_Containing_Zlint");

        //Lint certificate,crl 
        LintResult lintResult = ZLinter.lint("certificate.cer", ZLinter.Format.pem);

        //validate result
        System.out.println(lintResult.isPassed());
    }
}
```

## Dependecies

It used Google Gson for JSON. https://mvnrepository.com/artifact/com.google.code.gson/gson

## Contributing

We welcome contributions! Please fork the repository and submit pull requests.

## License

This project is licensed under the MIT License. See the LICENSE file for details.

## Contact

For any questions or issues, please open an issue on GitHub.