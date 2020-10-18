package org.feixu.geer.biz.helper

import com.helger.commons.annotation.Nonempty
import com.helger.font.api.EFontStyle
import com.helger.font.api.EFontType
import com.helger.font.api.EFontWeight
import com.helger.font.api.FontResource
import com.helger.font.api.IFontResource
import com.helger.font.api.IFontStyle
import com.helger.font.api.IFontWeight
import com.helger.font.api.IHasFontResource

import javax.annotation.Nonnull

public enum MyFontResource implements IHasFontResource {
    MICROSOFT_YAHEI("微软雅黑", EFontType.TTF, EFontStyle.REGULAR, EFontWeight.REGULAR, "meterial/msyh.ttf");

    private final FontResource m_aRes;

    private MyFontResource(@Nonnull @Nonempty final String sFontName,
                           @Nonnull final EFontType eFontType,
                           @Nonnull final IFontStyle aFontStyle,
                           @Nonnull final IFontWeight aFontWeight,
                           @Nonnull @Nonempty final String sPath) {
        this.m_aRes = new FontResource(sFontName, eFontType, aFontStyle, aFontWeight, sPath);
    }

    @Nonnull
    public IFontResource getFontResource() {
        return this.m_aRes;
    }
}
