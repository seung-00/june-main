package june.intrerface

import june.domain.Funding

data class FundingView(
    val id: String,
    val title: String,
    val description: String,
    val author: String,
    val age: String,
    val size: String,
    val gender: String,
    val region: String,
    val budget: Int,
    val amount: Int,
    val participants: List<String>,
    val imageUrl: String?,
    val hashtags: List<String>,
)

fun Funding.toView(): FundingView = FundingView(
    id = id.value,
    title = title,
    description = description,
    author = author,
    age = age,
    size = size,
    gender = gender,
    region = region,
    budget = budget,
    amount = amount,
    participants = participants,
    imageUrl = if (imageUrl.isNullOrEmpty()) null else imageUrl,
    hashtags = hashtags,
)