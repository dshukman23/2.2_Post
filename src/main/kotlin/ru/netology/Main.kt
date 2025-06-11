package ru.netology

// ========== Основной класс Post ==========
data class Post(
    val id: Int,
    val ownerId: Int?,
    val fromId: Int?,
    val date: Long?,
    var text: String?,
    val friendsOnly: Boolean? = null,
    val comments: Comments? = null,
    val likes: Likes? = null,
    val viewsCount: Int? = null,
    val postType: String? = "post",
    val canPin: Boolean? = null,
    val canDelete: Boolean? = null,
    val canEdit: Boolean? = null,
    val isPinned: Boolean? = null,
    val markedAsAds: Boolean? = null,
    val isFavorite: Boolean? = null,
    val attachments: Array<Attachment>? = null // Новое поле
)

// ========== Вспомогательные классы для Post ==========
data class Comments(
    val count: Int = 0,
    val canPost: Boolean = true,
    val groupsCanPost: Boolean = false,
    val canClose: Boolean = false,
    val canOpen: Boolean = false
)

data class Likes(
    val count: Int = 0,
    val userLikes: Boolean = false,
    val canLike: Boolean = true,
    val canPublish: Boolean = true
)

// ========== Поддержка Attachments (вложений) ==========
abstract class Attachment(val type: String)

// Класс для изображения (обложки и первый кадр)
data class Image(
    val height: Int,
    val width: Int,
    val url: String,
    val withPadding: Boolean? = null
)

// Конкретный тип вложения — видео
data class VideoAttachment(val video: Video) : Attachment("video")

data class Video(
    val id: Int,
    val ownerId: Int,
    val title: String,
    val description: String? = null,

    val duration: Int,

    val image: List<Image>? = null,
    val firstFrame: List<Image>? = null,

    val date: Long? = null,
    val views: Int? = null,

    val player: String? = null,

    val canLike: Boolean? = null,
    val canComment: Boolean? = null,
    val canRepost: Boolean? = null,

    val likes: Likes? = null
)

// ========== WallService для работы с постами ==========
class WallService {
    private val posts = mutableListOf<Post>()
    private var nextId = 1

    fun add(post: Post): Post {
        val newPost = post.copy(id = nextId++)
        posts.add(newPost)
        return newPost
    }

    fun update(post: Post): Boolean {
        val index = posts.indexOfFirst { it.id == post.id }
        if (index != -1) {
            posts[index] = post
            return true
        }
        return false
    }

    fun clear() {
        posts.clear()
        nextId = 1
    }

    fun getPosts(): List<Post> = posts
}

// ========== main() для тестирования ==========
fun main() {
    val wallService = WallService()

    val imageList = listOf(
        Image(height = 1080, width = 1920, url = "https://vk.com/video_preview.jpg")
    )

    val video = Video(
        id = 123456789,
        ownerId = 100,
        title = "Как программировать на Kotlin",
        description = "Подробный урок по основам Kotlin",
        duration = 600,
        image = imageList,
        firstFrame = imageList,
        date = System.currentTimeMillis() / 1000,
        views = 1500,
        player = "https://vk.com/video_player.html?video=100_123456789",
        canLike = true,
        canComment = true,
        canRepost = true,
        likes = Likes(count = 120, userLikes = false)
    )

    val post = Post(
        id = 0,
        ownerId = 100,
        fromId = 100,
        date = System.currentTimeMillis() / 1000,
        text = "Смотрите новое видео!",
        attachments = arrayOf(VideoAttachment(video))
    )

    val savedPost = wallService.add(post)
    println("Сохранённый пост: $savedPost")
}