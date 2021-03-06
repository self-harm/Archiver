package io.archive;

public class FileProperties {
    private String name;
    private long size;
    private long compressedSize;
    private int compressionMethod;

    public FileProperties(String name, long size, long compressedSize, int compressionMethod) {
        this.name = name;
        this.size = size;
        this.compressedSize = compressedSize;
        this.compressionMethod = compressionMethod;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public long getCompressedSize() {
        return compressedSize;
    }

    public int getCompressionMethod() {
        return compressionMethod;
    }

    public long getCompressionRatio(){
        return 100-((compressedSize*100)/size);
    }

    public String toString(){
        if(size>0){
            return name + " " + size/1024 + " Kb (" + compressedSize/1024 + " Kb) сжатие: " + getCompressionRatio() + "%";
        }
        else return name;
    }
}
