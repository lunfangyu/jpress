/**
 * Copyright (c) 2016-2019, Michael Yang 杨福海 (fuhai999@gmail.com).
 * <p>
 * Licensed under the GNU Lesser General Public License (LGPL) ,Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jpress.module.page.model;

import com.jfinal.core.JFinal;
import io.jboot.db.annotation.Table;
import io.jboot.utils.StrUtil;
import io.jpress.JPressConsts;
import io.jpress.JPressOptions;
import io.jpress.commons.utils.JsoupUtils;
import io.jpress.commons.utils.MarkdownUtils;
import io.jpress.module.page.model.base.BaseSinglePage;
import io.jpress.web.seoping.PingData;

import java.util.List;

/**
 * Generated by Jboot.
 */
@Table(tableName = "single_page", primaryKey = "id")
public class SinglePage extends BaseSinglePage<SinglePage> {

    public static final String STATUS_NORMAL = "normal";
    public static final String STATUS_DRAFT = "draft";
    public static final String STATUS_TRASH = "trash";


    public boolean isNormal() {
        return STATUS_NORMAL.equals(getStatus());
    }

    public boolean isDraft() {
        return STATUS_DRAFT.equals(getStatus());
    }

    public boolean isTrash() {
        return STATUS_TRASH.equals(getStatus());
    }


    public String getHtmlView() {
        return StrUtil.isBlank(getStyle()) ? "page.html" : "page_" + getStyle().trim() + ".html";
    }

    public String getUrl() {

        if (StrUtil.isBlank(getSlug())) {
            return JFinal.me().getContextPath() + "/" + getId() + JPressOptions.getAppUrlSuffix();
        } else {
            return JFinal.me().getContextPath() + "/" + getSlug() + JPressOptions.getAppUrlSuffix();
        }
    }


    public String getText() {
        return StrUtil.escapeHtml(JsoupUtils.getText(getContent()));
    }

    @Override
    public String getContent() {
        String content = super.getContent();
        if (_isMarkdownMode()) {
            content = MarkdownUtils.toHtml(content);
            content = JsoupUtils.clean(content);
        }
        return content;
    }

    public boolean _isMarkdownMode() {
        return JPressConsts.EDIT_MODE_MARKDOWN.equals(getEditMode());
    }

    public String getOrignalContent(){
        return super.getContent();
    }

    public List<String> getImages() {
        return JsoupUtils.getImageSrcs(getContent());
    }


    public String getFirstImage() {
        return JsoupUtils.getFirstImageSrc(getContent());
    }


    public PingData toPingData() {
        return PingData.create(getTitle(), getUrl());
    }
}
