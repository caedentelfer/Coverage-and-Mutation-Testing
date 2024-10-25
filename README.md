# Knapsack Solver with Coverage and Mutation Testing

#### Caeden Telfer

# Project Updates: JaCoCo and PIT Report Generation with GitLab Pages
### Updated 19 October 2024


## Overview of changes (JaCoCo and PIT Report Generation)
The project now includes automated generation and publication of JaCoCo code coverage reports and PIT mutation testing reports. The reports are automatically generated as part of the CI pipeline and are published to GitLab Pages for easy access.

## Key Changes

### 1. **Automated Report Generation**:
  - JaCoCo reports are generated to track code coverage, providing insights into how much of the codebase is covered by tests.
  - PIT (PIT Mutation Testing) reports are generated to assess the quality of the tests by checking their ability to detect intentional defects.


### 2. **GitLab CI Pipeline Configuration:**
  - The .gitlab-ci.yml file was updated to include a combined stage (report-and-pages) that:
    -Runs tests and generates JaCoCo and PIT reports.
    - Moves the generated reports into the public/ directory at the root level of the project.
  - A pages stage was added to deploy the contents of the public/ directory to GitLab Pages.
  - The pipeline now consists of four stages: build, test, report-and-pages, and pages.


![Alt Text](https://www.nagarro.com/hs-fs/hubfs/Blog-Illustration%20-%20Code%20Coverage.png?width=3125&height=1750&name=Blog-Illustration%20-%20Code%20Coverage.png)




## (Older versions below unchanged)

## Project Structure

- **`src/main/java/com/truckpacker`**: Contains the main source code.
    - **`Main.java`**: Entry point of the application.
    - **`KnapsackSolver.java`**: Solves the Knapsack problem.
    - **`GoodsItem.java`**: Defines the goods items used in the knapsack.
    - **`Store.java`**: Represents a store and its requested goods.
- **`src/test/java/com/truckpacker`**: Contains unit tests.
    - **`Test.java`**: Includes basic tests for the truckpacker knapsack.
- **`input/`**: Directory for input JSON files.
- **`output/`**: Directory for output files.
- **`target/`**: Maven build output (generated during build process).

## Setup


### Prerequisites

- Java 16
- Maven
- Git

### Cloning the Repository

To clone the repository and navigate into the project directory, run:

```bash
git clone <repository-url>
cd tut-project
```

## Usage

### Set Maven classpath (if `mvn` isn't recognised in the terminal):

```bash
export M2_HOME=/opt/idea-IC-223.8617.56/plugins/maven/lib/maven3
```

Then run:
```bash
export PATH=$M2_HOME/bin:$PATH
```

### To test hard-coded test files (located inside /input)

Inside the root directory (tut-project):
```bash
mvn clean package
```
Then run:
```bash
java -cp "target/classes:$(find ~/.m2/repository -name '*.jar' | tr '\n' ':')" com.truckpacker.Main
```

To add new test cases:

- Use the .json format explained at the end of this README.
- Test cases are read in using `"input/test" + testno + "_goods.json")`
- Change line 19 in Main.java: `int testno = 1;`. for your test case number. Remember to include test`num`_goods.json and test`num`_,axvolume.json

### To test using test-suite on pipeline:

Inside the root directory (tut-project):
```bash
mvn clean package
```
Then run:
```bash
mvn test
```

# File format:

## Input

### Now accepts both **yaml** and **json** input formats

### .json:

Input files inside **/input** labeled test`number`_goods.json:

E.g.
```bash
{
    "maxItems": 5,
    "goods": [
      { "name": "item1", "volume": 3, "value": 10 },
      { "name": "item2", "volume": 2, "value": 5 },
      { "name": "item3", "volume": 6, "value": 12 },
      { "name": "item4", "volume": 2, "value": 3 },
      { "name": "item5", "volume": 4, "value": 100 }
    ]
  }
```

test`number`_maxvolume.json:

E.g.
```bash
{
    "maxVolume": 40
}
```

test`number`_stores.json:

```bash
{
    "stores": [
        {
            "name": "Store1",
            "requestedGoods": [
                { "name": "item1", "volume": 3, "value": 10 },
                { "name": "item3", "volume": 6, "value": 12 }
            ]
        },
        {
            "name": "Store2",
            "requestedGoods": [
                { "name": "item2", "volume": 2, "value": 5 },
                { "name": "item4", "volume": 2, "value": 3 }
            ]
        }
    ]
}
```

### .yaml:

Input files inside **/input** labeled test`number`_goods.yaml:

E.g.
```bash
maxItems: 5
goods:
- name: item1
  volume: 2
  value: 3
- name: item2
  volume: 3
  value: 4

```

test`number`_maxvolume.yaml:

E.g.
```bash
maxVolume: 10
```

test`number`_stores.yaml:

```bash
- name: Store1
  requestedItems:
  - item1
- name: Store2
  requestedItems:
  - item2

```

## Output
Output .txt files inside **/output** (created when running without test suite)
Output files show the optimal list of goods to be packed with their attributes

E.g.
```bash
Packed items:
name = 'item3', volume = 6, value = 12, maxCount = 5
name = 'item3', volume = 6, value = 12, maxCount = 5
name = 'item1', volume = 3, value = 10, maxCount = 5
name = 'item1', volume = 3, value = 10, maxCount = 5
name = 'item1', volume = 3, value = 10, maxCount = 5

Stores to visit:
Store1
```