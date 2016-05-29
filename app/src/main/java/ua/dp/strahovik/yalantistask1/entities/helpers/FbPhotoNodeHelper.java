package ua.dp.strahovik.yalantistask1.entities.helpers;


import java.util.List;

public class FbPhotoNodeHelper {

    private List<PlatformImageSource> mPlatformImageSources;

    public static PlatformImageSource platformImageSourceFactory(int height, String source, int width) {
        return new PlatformImageSource(height, source, width);
    }

    public static class PlatformImageSource {

        private String source;
        private int height, width;

        public PlatformImageSource(int height, String source, int width) {
            this.height = height;
            this.source = source;
            this.width = width;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        @Override
        public String toString() {
            return "PlatformImageSource{" +
                    "source='" + source + '\'' +
                    ", height=" + height +
                    ", width=" + width +
                    "\n}";
        }
    }

    public List<PlatformImageSource> getPlatformImageSources() {
        return mPlatformImageSources;
    }

    public void setPlatformImageSources(List<PlatformImageSource> platformImageSources) {
        mPlatformImageSources = platformImageSources;
    }

    @Override
    public String toString() {
        return "FbPhotoNodeHelper{" +
                "mPlatformImageSources=" + mPlatformImageSources +
                "\n}";
    }
}
