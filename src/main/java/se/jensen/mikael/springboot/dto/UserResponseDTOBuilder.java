package se.jensen.mikael.springboot.dto;


public final class UserResponseDTOBuilder {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String displayName;
    private String bio;
    private String profileImagePath;

    /**
     * Används för att bygga ett UserResponseDTO objekt
     * utan att behöva skicka in alla fält i konstruktorn på en gång.
     */
    private UserResponseDTOBuilder() {
    }

    public static UserResponseDTOBuilder builder() {
        return new UserResponseDTOBuilder();
    }


    public UserResponseDTOBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public UserResponseDTOBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public UserResponseDTOBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserResponseDTOBuilder withRole(String role) {
        this.role = role;
        return this;
    }

    public UserResponseDTOBuilder withDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public UserResponseDTOBuilder withBio(String bio) {
        this.bio = bio;
        return this;
    }

    public UserResponseDTOBuilder withProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
        return this;
    }

    public UserResponseDTO build() {
        return new UserResponseDTO(
                id,
                username,
                email,
                role,
                displayName,
                bio,
                profileImagePath
        );
    }
}

