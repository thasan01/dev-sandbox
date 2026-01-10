FROM alpine:3.12

# Set environment
ENV LUA_VERSION=5.1.5
ENV LUAROCKS_VERSION=3.4.0


RUN set -xe
RUN apk add --virtual .build-deps  curl gcc g++ libc-dev make readline-dev unzip openssl
RUN apk add --no-cache readline

#install lua
RUN wget http://www.lua.org/ftp/lua-${LUA_VERSION}.tar.gz
RUN tar zxf lua-${LUA_VERSION}.tar.gz

#build lua
RUN cd lua-${LUA_VERSION} && make -j $(getconf _NPROCESSORS_ONLN) linux && make install
RUN cd /

# install luarocks
RUN wget https://luarocks.org/releases/luarocks-${LUAROCKS_VERSION}.tar.gz
RUN tar zxf luarocks-${LUAROCKS_VERSION}.tar.gz
RUN cd luarocks-${LUAROCKS_VERSION} && ./configure && make -j $(getconf _NPROCESSORS_ONLN) build && make install

RUN luarocks install luasocket
RUN cd /

# cleanup
RUN rm -f lua-${LUA_VERSION}.tar.gz
RUN rm -rf lua-${LUA_VERSION}
RUN rm -f luarocks-${LUAROCKS_VERSION}.tar.gz
RUN rm -rf luarocks-${LUAROCKS_VERSION}
RUN apk del .build-deps
