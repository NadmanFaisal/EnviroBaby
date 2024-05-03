# Credits to Pasha Klimenkov for writing esp32 CI guide: https://codeblog.dotsandbrackets.com/gitlab-ci-esp32-arduino/
# I have modified his code to work with Wio Terminal, which is not the esp32, but still uses arduino-cli and GitLab.

#!/bin/bash

apt-get update
cd ~

# Install arduino-cli
apt-get install curl -y
curl -fsSL https://raw.githubusercontent.com/arduino/arduino-cli/master/install.sh | sh
export PATH=$PATH:/root/bin
arduino-cli -version

# Install Seeed Wio Terminal core
printf "board_manager:\n  additional_urls:\n    - https://files.seeedstudio.com/arduino/package_seeeduino_boards_index.json\n" > .arduino-cli.yaml
arduino-cli core update-index --config-file .arduino-cli.yaml
arduino-cli core install Seeeduino:samd --config-file .arduino-cli.yaml

# Install 'native' packages (libraries that do not come with the core)
arduino-cli lib install "Seeed Arduino rpcWiFi@1.0.7"
arduino-cli lib install "DHT sensor library@1.4.6"
arduino-cli lib install "PubSubClient@2.8.0"

cd -

# Install 'third-party' packages / libraries: find proper location and 'git clone'
#apt-get install git -y
#cd `arduino-cli config dump | grep sketchbook | sed 's/.*\ //'`/libraries
#git clone https://github.com/ThingPulse/esp8266-oled-ssd1306.git
#git clone https://github.com/Seeed-Studio/Seeed_Arduino_RTC.git

#cd -
