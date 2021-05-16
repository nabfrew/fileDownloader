# Usage: 
> fileDownloader [-d, -directory] [-url] [-v, -verbose]

Options:

- `-d`, `-directory`
    - Directory to download to. 
        - Default: `[pwd]`.

- `-url`
    - Path to text file containing urls to download.
        - Default: `[pwd]/urlList.txt`.

- -`v`, -`verbose`
    - [`t`, `true`]/[`f`, `false`]
        - Default: `true`

- `-h`, `-help`
 
  Display this message.

# Deploy
Any host with docker installed should be able to drop the jar in the docker folder (manually, so far), 
configure the`.env` file, and run 
> docker-compose build

and then

> docker-compose up

or

> docker-compose run file_downloader

To trigger a download using the configured source/destination locations.

With the default configuration, the files in the example source files should download to `[root]/Docker/downloads`. 
There's some work to be done, but the pieces are place to automate a pipeline with packaging, build docker image 
and push to a repository, to be pulled and used in production.

#Limitations/Further work 

I have assumed the downloading does not take more than 5 min. Further development would
be to find a solution that allows concurrent downloads, and a JSON input might make more sense than a local .txt file
in production.
