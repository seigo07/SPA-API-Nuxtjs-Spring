# Project

Spring + Nuxtjs API and SPA system.

## Description

This is a test API and SPA using Spring boot framework for API and Nuxtjs for SPA.

<img width="1787" alt="Screenshot 2023-03-12 at 01 36 34" src="https://user-images.githubusercontent.com/118636537/224519771-90adfc94-dc73-460c-b086-a91a5ece805f.png">

<img width="1789" alt="Screenshot 2023-03-12 at 01 52 48" src="https://user-images.githubusercontent.com/118636537/224519859-cf231b84-8cf8-4009-a2bf-93e914ff8d5c.png">

## API Documentation

https://documenter.getpostman.com/view/23875384/2s93CSqBLL

## Getting started API server

### Running API server local

```
cd backend
mvn spring-boot:run
```

## Getting started Web-UI

### for mac

### Install and setup NodeBrew


```
brew install nodebrew
nodebrew setup
## Check shell
echo $SHELL
vi ~/.bashrc or vi ~/.zshrc
## adding path to the above setting file
export PATH=$HOME/.nodebrew/current/bin:$PATH
## Reflecting changes
source ~/.bashrc or source ~/.zshrc
```

### Install Node.js and yarn

```
nodebrew install-binary stable
nodebrew use stable
brew install yarn --ignore-dependencies 
```

### for lab's Linux server

### Install and setup yarn


```
# node.js and npm might be installed already in lab's server.

# Check node.js and npm
node -v
npm -v
npm install -g yarn
yarn -v
yarn init
```

### Running Web-UI in local

```
## if you don't have package.json
cd bus_travel
npm install
yarn dev
```

### In case of error

Please run the below command if you got this error.
error:0308010C:digital envelope routines::unsupported

```
export NODE_OPTIONS=--openssl-legacy-provider
```

### How to deploy Web-UI to lab's server

```
cd bus_travel
yarn run generate
```
Then, dist directory will be generated. 
Upload all files under a dist directory to /cs/home/$USER/nginx_default (must not include dist directory). 
Then, we can access Web-UI from the link below.

```
https://$USER.host.cs.st-andrews.ac.uk/
```

You need to fix codes to avoid CORS problrem between SPA and server.
Please fix nux.config.js based on your server host and port number.
```
nuxt.config.js: L:51-56
Please fix target URL based on the server you use.

proxy: {
    '/stop/': 'http://localhost:8080',
    '/route/': 'http://localhost:8080',
},
```
