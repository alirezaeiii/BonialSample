package com.android.sample.bonial.response

import com.android.sample.bonial.domain.Brochure
import com.android.sample.bonial.domain.BrochureConverter
import com.squareup.moshi.Json

sealed class Content(@Json(name = "contentType") val type: ContentType) {

    data class NetworkBrochure(
        val content: BrochureContent
    ) : Content(ContentType.brochure), BrochureConverter {
        override fun convert(): Brochure {
            return Brochure(
                id = content.id,
                image = content.brochureImage,
                retailerName = content.retailer?.name ?: "Name not found!",
                distance = content.distance,
                isPremium = false
            )
        }
    }

    data class BrochurePremium(
        val content: BrochureContent
    ) : Content(ContentType.brochurePremium), BrochureConverter {
        override fun convert(): Brochure {
            return Brochure(
                id = content.id,
                image = content.brochureImage,
                retailerName = content.retailer?.name ?: "Name not found!",
                distance = content.distance,
                isPremium = true
            )
        }
    }

    class CashbackOffersV2 : Content(ContentType.cashbackOffersV2)

    class RateUs: Content(ContentType.rateUs)

    class SuperBannerCarousel: Content(ContentType.superBannerCarousel)

    class StoryCarousel: Content(ContentType.storyCarousel)

    class BlogCarousel : Content(ContentType.blogCarousel)

    class Referral : Content(ContentType.referral)
}