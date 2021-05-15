# Usage: 
fileDownloader [-d, -directory] [-url] [-v, -verbose]

Options:

## [-d, -directory]

  Directory to download to. Default: [present working directory].

## [-url]

  Path to text file containing urls to download. Default: [pwd]/urlList.txt.

## [-v, -verbose]

  [t, true]/[f, false]

## [-h, -help]
 
  Display this message.

# Deploy
Any host with docker installed should be able to drop the jar in the docker folder (manually, so far), configure the .env file, and run 
> docker-compose build

and then

> docker-compose up

or

> docker run file_downloader

to trigger a download using the configured source/destination locations.

with the default configuration, the files in the exampe source files should download to [root]/Docker/downloads. There's some work to be done, but the pieces are place to automate a pipeline with packaging, build docker image and push to a repository, to be pulled and used in production.. 
