# FNX Admin skeleton

This is the basis of an FNX administration. It is written in dart and angular2.

The UI uses [fnx_ui](https://github.com/fnx-io/fnx_ui).

## Development

You will need to install dart and dartium for seamless developer experience

### Instalation of dart & dartium
#### Mac OS with homebrew:

Run `brew install dart --with-dartium`. Dartium can then be launched from command line
using the command `open -a /usr/local/opt/dart/Chromium.app`.

#### Other environments

Download appropriate bundle from [https://www.dartlang.org/install](https://www.dartlang.org/install)

## Installing dependencies

In the admin directory of this repository, run `pub get`. This will fetch all required
dependencies of this application.

## Running dev version of the application

In an terminal type `pub serve`. This will start a server which will wait for connection
from an browser to serve the application. Now you can start Dartium and connect to the port
(default `8080`) and start developing.
