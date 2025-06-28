package june.application

data class DraftSucceed(
    val title: String,
    val content: String,
)

data class ImageSucceed(
    val imageUrl: String,
    val prompt: String,
)

data class HashTagSucceed(
    val hashtags: List<String>,
)

data class FundingItem(
    val draft: DraftSucceed,
    val image: ImageSucceed,
    val hashtag: List<String>,
)
