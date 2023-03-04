module com.nivekaa.service {
  exports com.nivekaa.service.spi;

  provides java.nio.file.spi.FileSystemProvider with
      com.nivekaa.service.spi.RemoteFileSystemProvider;
}
