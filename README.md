[![Docker Image Build](https://github.com/fabriziodsp-ransom/home-cloud-server/actions/workflows/docker-image.yml/badge.svg)](https://github.com/fabriziodsp-ransom/home-cloud-server/actions/workflows/docker-image.yml)

# home-cloud-server
A small & home-made Java server to access the host's files from the private network. Like a private Google Drive ;)

## What it does?
Lets you download the files and explore the directories of the host machine. On port 8080 by default. You can use the Apache Tomcat (v10.1.6) server attached on the latest release to host this "_home cloud_".

## How it works?
Via making `GET` requests to two endpoints: `show-directory-contents` & `download` using a `p` parameter specifying the path.

## Endpoints:
- `/download`:
    - METHODS: 
      - `GET`: `/download?p="full file path"`
    - Example: `http://localhost:8080/home-cloud-server-0.1/download?p=C:/Users/<username>/Desktop/test.txt`
    - Example response:
      - `Error 404 (Not found)`: In case the specified route doesn't exist.
      - `Error 400 (bad request)`: Can happen when the request is malformed or the path references a directory and not a file. (Can't download entire directories for now).
      - `Status 200`: File was found and a download should begin now.
- `/show-directory-contents`:
    - METHODS:
      - `GET`: `/show-directory-contents?p=C:/Users/<username>/Desktop/`
    - Example: `http://localhost:8080/home-cloud-server-0.1/show-directory-contents?p=C:/Users/usuario/Desktop/`
    - Example response:
      - `Error 404 (Not found)`: In case the specified route doesn't exist.
      - `Error 400 (bad request)`: Can happen when the request is malformed or the path references a `file` and not a `directory`. (Can't navigate inside a file).
      - `Status 200`: Specified path is correct and a JSON response, as shown below, will be sent.
      
## Server responses
### JSON structure for ERROR responses (both endpoints):
```
{
  "error": true,
  "message": "<error message>"
}
```

### JSON structure for `show-directory-contents` endpoint:
```
{
  "current_dir": "/the/absolute/path/for/the/current/directory" // To be used for better concatenations, in case you want to create a client for this server afterwards.
  "files": [...] // Array containing filenames of this path.
  "folders": [...] // Array containing folders of this path.
}
```
### The `/download` endpoint will only answer with a binary file to download (in case its found).
