# Zombie Hunt — pseudo-3D Java Swing shooter
# Build:  docker build -t zombie-hunt .
# Play:   docker run --rm -p 6080:6080 zombie-hunt
#         then open http://localhost:6080/vnc.html
FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /src
COPY build.sh ./
COPY src ./src
COPY Zombie ./Zombie
COPY Gun ./Gun
COPY Obsticales ./Obsticales
RUN chmod +x build.sh && ./build.sh

FROM eclipse-temurin:17-jre-jammy

RUN apt-get update \
  && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libfreetype6 \
    fontconfig \
    libasound2 \
    xvfb \
    x11-utils \
    x11-xserver-utils \
    x11vnc \
    fluxbox \
    novnc \
    websockify \
  && rm -rf /var/lib/apt/lists/* \
  && printf '%s\n' '#!/bin/sh' 'exec xsetroot -solid "#101010"' > /usr/local/bin/fbsetbg \
  && chmod +x /usr/local/bin/fbsetbg \
  && mkdir -p /root/.fluxbox \
  && printf '%s\n' \
       'session.screen0.rootCommand: xsetroot -solid \#101010' \
       'session.screen0.toolbar.visible: false' \
       > /root/.fluxbox/init

COPY docker/novnc-pointer-lock.js /usr/share/novnc/app/zombie-hunt-pointer-lock.js
COPY docker/novnc-audio.js /usr/share/novnc/app/zombie-hunt-audio.js
COPY files/sounds /usr/share/novnc/sounds
RUN sed -i '/src="app\/ui.js"/a\\    <script type="module" src="app/zombie-hunt-pointer-lock.js"></script>' /usr/share/novnc/vnc.html \
  && sed -i '/src="app\/zombie-hunt-pointer-lock.js"/a\\    <script type="module" src="app/zombie-hunt-audio.js"></script>' /usr/share/novnc/vnc.html

WORKDIR /app
COPY --from=build /src/build/classes ./build/classes
COPY files ./files
COPY docker/entrypoint.sh /entrypoint.sh
RUN sed -i 's/\r$//' /entrypoint.sh && chmod +x /entrypoint.sh

EXPOSE 6080 5900
ENV ZH_DISPLAY_MODE=vnc \
    DISPLAY=:99 \
    SCREEN_SIZE=1280x800x24

ENTRYPOINT ["/entrypoint.sh"]
