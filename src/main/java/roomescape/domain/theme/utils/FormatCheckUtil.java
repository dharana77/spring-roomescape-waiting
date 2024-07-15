package roomescape.domain.theme.utils;

import roomescape.domain.theme.error.exception.ThemeErrorCode;
import roomescape.domain.theme.error.exception.ThemeException;

public class FormatCheckUtil {

    private static final String THEME_NAME_FORMAT = "^[A-Za-z0-9가-힣\\s]{8,20}$";
    private static final String THEME_DESCRIPTION_FORMAT = "^[A-Za-z0-9가-힣\\s]{1,200}$";
    private static final String THEME_THUMBNAIL_FORMAT = "^(https?:\\/\\/)?([a-zA-Z0-9.-]+)(:[0-9]{1,5})?(\\/[^\\s]*)?$";

    public static void themeNameFormatCheck(String name) {
        if (!name.matches(THEME_NAME_FORMAT)) {
            throw new ThemeException(ThemeErrorCode.INVALID_THEME_NAME_FORMAT_ERROR);
        }
    }

    public static void themeDescriptionCheck(String description) {
        if (!description.matches(THEME_DESCRIPTION_FORMAT)) {
            throw new ThemeException(ThemeErrorCode.INVALID_THEME_DESCRIPTION_FORMAT_ERROR);
        }
    }

    public static void themeThumbnailFormatCheck(String thumbnail) {
        if (!thumbnail.matches(THEME_THUMBNAIL_FORMAT)) {
            throw new ThemeException(ThemeErrorCode.INVALID_THEME_THUMBNAIL_FORMAT_ERROR);
        }
    }
}
