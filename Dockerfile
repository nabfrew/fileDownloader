FROM openJDK:16

COPY target/fileDownloader-0.1-SNAPSHOT.jar /fileDownloader.jar

ENTRYPOINT ["file_downloader"]
