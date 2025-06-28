package june.intrerface

import june.domain.Funding
import june.domain.FundingRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["*"])
@RestController
class FundingController(
    private val fundingRepository: FundingRepository
) {

    data class CreateFunding(
        val title: String,
        val description: String,
        val author: String,
        val age: String,
        val size: String,
        val gender: String,
        val region: String,
        val budget: Int,
        val imageUrl: String?,
        val hashtags: List<String>,
    )

    data class JoinFunding(
        val participantId: String,
        val fee: Int,
    )

    @PostMapping("/api/funding")
    fun createFunding(@RequestBody data: CreateFunding): ResponseEntity<MessageResponse> {
        val funding = Funding.initialize(
            title = data.title,
            description = data.description,
            author = data.author,
            age = data.age,
            size = data.size,
            gender = data.gender,
            region = data.region,
            budget = data.budget,
            imageUrl = data.imageUrl,
            hashtags = data.hashtags,
        )

        val id = fundingRepository.save(funding)

        return ResponseEntity.ok(MessageResponse(id.value))
    }

    @GetMapping("/api/funding/{id}")
    fun getFunding(@PathVariable id: String): ResponseEntity<FundingView> {
        val funding = fundingRepository.find(Funding.Id(id))

        return funding?.let { ResponseEntity.ok(it.toView()) } ?: ResponseEntity.notFound().build()
    }

    @GetMapping("/api/funding")
    fun getAllFunding(): ResponseEntity<List<FundingView>> {
        val all = fundingRepository.findAll().map(Funding::toView)

        return ResponseEntity.ok(all)
    }

    @DeleteMapping("/api/funding/{id}")
    fun deleteFunding(@PathVariable id: String): ResponseEntity<Void> {
        fundingRepository.delete(Funding.Id(id))
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/api/funding")
    fun deleteAllFunding(): ResponseEntity<Void> {
        fundingRepository.findAllId().forEach { fundingRepository.delete(Funding.Id(it)) }
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/api/funding/{id}/join")
    fun joinFunding(@PathVariable id: String, @RequestBody body: JoinFunding): ResponseEntity<MessageResponse> {
        val funding = fundingRepository.find(Funding.Id(id)) ?: return ResponseEntity.notFound().build()

        if (funding.participants.contains(body.participantId)) {
            return ResponseEntity.ok(MessageResponse("이미 참여했습니다."))
        }

        if (funding.budget < funding.amount + body.fee) {
            return ResponseEntity.badRequest().body(MessageResponse("목표 금액을 초과했습니다."))
        }

        val updatedFunding =
            funding.copy(participants = funding.participants + body.participantId, amount = funding.budget + body.fee)

        val id = fundingRepository.update(updatedFunding)

        return ResponseEntity.ok(MessageResponse(id.value))
    }
}