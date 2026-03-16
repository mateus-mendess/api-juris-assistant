package br.com.juristrack.Juris.Track.enums;

public enum FileType {
    AVATAR("avatars/"),
    POWER_OF_ATTORNEY("powers-of-attorney/"),
    DECLARATION_TERM("declaration-terms/"),
    CONTRACT("contract/");

    private final String folder;

    FileType(String folder) {
        this.folder = folder;
    }

    public String getFolder() {
        return folder;
    }
}
