# Replication package for the normalization and recursion analysis of JSON schemas from the
# TestSuite, SchemaStore and SchemaCorpus.
FROM ubuntu:21.04

MAINTAINER Lukas Ellinger "elling09@ads.uni-passau.de"

ENV DEBIAN_FRONTEND noninteractive
ENV LANG="C.UTF-8"
ENV LC_ALL="C.UTF-8"

RUN apt update && apt -y dist-upgrade

RUN apt install -y --no-install-recommends \
    ca-certificates \
  	git \
    sudo \
    openjdk-16-jre-headless \
    maven

# Obtain schemas from the TestSuite to be normalized.
# These are stored in /home/TestSuiteDraft4/TestSuiteSchemas/.
WORKDIR /home/
RUN git clone --no-checkout https://github.com/json-schema-org/JSON-Schema-Test-Suite.git TestSuiteDraft4
WORKDIR TestSuiteDraft4/
RUN git config core.sparseCheckout true
RUN echo "tests/draft4/*">> .git/info/sparse-checkout
RUN echo "remotes/*">> .git/info/sparse-checkout
RUN git checkout 0c223de
RUN mv tests/draft4 TestSuiteSchemas
RUN rm -r tests/

# Obtain schemas from the SchemaStore to be normalized.
# These are stored in /home/SchemaStore/SchemaStoreSchemas/.
WORKDIR /home/
RUN git clone --no-checkout https://github.com/SchemaStore/schemastore.git SchemaStore
WORKDIR SchemaStore/
RUN git config core.sparseCheckout true
RUN echo "src/schemas/json/*"> .git/info/sparse-checkout
RUN git checkout 2ad0b3d
RUN mv src/schemas/json/ SchemaStoreSchemas
RUN rm -r src/

# Obtain schemas from the SchemaCorpus to be normalized.
# These are stored in /home/SchemaCorpus/SchemaCorpusSchemas/.
WORKDIR /home/
RUN git clone --no-checkout https://github.com/sdbs-uni-p/json-schema-corpus.git SchemaCorpus
WORKDIR SchemaCorpus/
RUN git config core.sparseCheckout true
RUN echo "json_schema_corpus/*">> .git/info/sparse-checkout
RUN echo "repos_fullpath.csv">> .git/info/sparse-checkout
RUN git checkout 9c0e796
RUN mv json_schema_corpus SchemaCorpusSchemas

# Obtain code from github and create jar-file.
# It is stored in /home/Code/.
WORKDIR /home/
RUN git clone https://github.com/sdbs-uni-p/jsonschema-refexpander.git Code
WORKDIR Code/
RUN git checkout 7dc2223
RUN mvn package

# Obtain schemas which get referenced in the schemas of TestSuite, SchemaStore and SchemaCorpus and
# csv-files in which each referenced uri has an assigned file-name.
# This is necessary to keep the normalization process of these three repositories reproducible.
WORKDIR /home/
RUN git clone --no-checkout https://github.com/lukasellinger/JSON_Schema_Analysis_Store.git Store
WORKDIR Store/
RUN git config core.sparseCheckout true
RUN echo "TestSuiteDraft4/Store/*">> .git/info/sparse-checkout
RUN echo "TestSuiteDraft4/UriOfFiles.csv">> .git/info/sparse-checkout
RUN echo "SchemaStore/Store/*">> .git/info/sparse-checkout
RUN echo "SchemaStore/UriOfFiles.csv">> .git/info/sparse-checkout
RUN echo "SchemaCorpus/Store/*">> .git/info/sparse-checkout
RUN echo "SchemaCorpus/UriOfFiles.csv">> .git/info/sparse-checkout
RUN git checkout 3e7b60d

# Move referenced schemas and csv-files to the specific directories.
RUN mv TestSuiteDraft4/Store/ /home/TestSuiteDraft4/
RUN mv TestSuiteDraft4/UriOfFiles.csv /home/TestSuiteDraft4/
RUN mv SchemaStore/Store/ /home/SchemaStore/
RUN mv SchemaStore/UriOfFiles.csv /home/SchemaStore/
RUN mv SchemaCorpus/Store/ /home/SchemaCorpus/
RUN mv SchemaCorpus/UriOfFiles.csv /home/SchemaCorpus/

# Copy jar-file in to the different directories.
WORKDIR /home/
RUN cp Code/target/schema_recursion-1.0-jar-with-dependencies.jar TestSuiteDraft4/
RUN cp Code/target/schema_recursion-1.0-jar-with-dependencies.jar SchemaStore/
RUN cp Code/target/schema_recursion-1.0-jar-with-dependencies.jar SchemaCorpus/

# Run the normalization and gather statistics:
# 1. TestSuite
WORKDIR /home/TestSuiteDraft4/
RUN java -jar schema_recursion-1.0-jar-with-dependencies.jar -normalize -testsuite -true -false "TestSuiteSchemas/"
RUN java -jar schema_recursion-1.0-jar-with-dependencies.jar -stats "extractedSchemas_TestSuiteSchemas/" "Normalized_extractedSchemas_TestSuiteSchemas/"

# 2. SchemaStore
WORKDIR /home/SchemaStore/
RUN java -jar schema_recursion-1.0-jar-with-dependencies.jar -normalize -normal -true -false "SchemaStoreSchemas/"
RUN java -jar schema_recursion-1.0-jar-with-dependencies.jar -stats "SchemaStoreSchemas/" "Normalized_SchemaStoreSchemas/"

# 3. SchemaCorpus
WORKDIR /home/SchemaCorpus/
RUN java -jar schema_recursion-1.0-jar-with-dependencies.jar -normalize -corpus -true -false "SchemaCorpusSchemas/" "repos_fullpath.csv"
RUN java -jar schema_recursion-1.0-jar-with-dependencies.jar -stats "SchemaCorpusSchemas/" "Normalized_SchemaCorpusSchemas/"
