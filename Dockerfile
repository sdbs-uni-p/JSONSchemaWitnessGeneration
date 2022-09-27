# reproduction package for the article [Witness Generation for JSON Schema](http://arxiv.org/abs/2202.12849),
# by Lyes Attouche, Mohamed-Amine Baazizi, Dario Colazzo, Giorgio Ghelli, Carlo Sartiani, and Stefanie Scherzinger.
#
# Copyright 2021, Stefan Klessinger <stefan.klessinger@uni-passau.de>
# SPDX-License-Identifier: GPL-3.0

FROM ubuntu:20.04

MAINTAINER Stefan Klessinger <stefan.klessinger@uni-passau.de>

ENV DEBIAN_FRONTEND noninteractive
ENV LANG="C.UTF-8"
ENV LC_ALL="C.UTF-8"

# Install packages for experiments
RUN apt-get update && apt-get install -y --no-install-recommends \
                build-essential \
		git \
		gradle \
                libffi-dev \
                libssl-dev \
                maven \
		openjdk-11-jdk \
		openjdk-11-jre \
                patch \
		wget \
                zlib1g-dev 

# Install dev packages
RUN apt-get install -y --no-install-recommends \
		nano \
		sudo

# Install packages for chart generation
RUN apt-get update && apt-get install -y --no-install-recommends --purge \
                fonts-linuxlibertine \
                g++ \
                make \
                r-base-core \
                r-cran-dplyr \
                r-cran-ggplot2 \
                r-cran-hexbin \
                r-cran-httpuv \
                r-cran-htmltools \
                r-cran-htmlwidgets \
                r-cran-jsonlite \
                r-cran-mime \
                r-cran-tidyr \
                r-cran-miniui \
                r-cran-sourcetools \
                r-cran-yaml \
    && R -q -e "install.packages(c('extrafont', 'ggrepel', 'ggExtra', 'forcats', 'remotes'), quiet=TRUE)" \
    && R -q -e "library(remotes); remotes::install_version('Rttf2pt1', version = '1.3.8', quiet=TRUE)" 

RUN apt-get update && apt-get install -y --no-install-recommends --purge \
                bsdmainutils \
                colordiff \
                dos2unix \
                libbz2-dev 

RUN mkdir -p /opt/python3.9/ && cd /opt/python3.9/ && \
    wget https://www.python.org/ftp/python/3.9.7/Python-3.9.7.tgz && \
    tar xzf Python-3.9.7.tgz && cd Python-3.9.7 && \
    ./configure --enable-optimizations && make install 

# Add user
RUN useradd -m -G sudo -s /bin/bash repro && echo "repro:repro" | chpasswd
RUN usermod -a -G staff repro
USER repro
WORKDIR /home/repro

# Clone jsongenerator and checkout the version we used
RUN git clone https://github.com/jimblackler/jsongenerator/ \
    && (cd jsongenerator && git checkout 324d9c1853d41fd09c0c31184ff916d75373ad83)

# Clone jsonsubschema and checkout the version we used
RUN git clone https://github.com/IBM/jsonsubschema/ \
    && (cd jsonsubschema && git checkout 9413abe5bce2f1f94622e2ed756eaa2747f6479a)

RUN python3.9 -m pip install pandas

# Add artifacts directory (from host) to home directory
ADD --chown=repro:repro artifacts/ /home/repro

RUN dos2unix scripts/* && dos2unix doAll.sh

# Patch jsongenerator to allow testing on our datasets
RUN scripts/patch-jsongenerator.sh
