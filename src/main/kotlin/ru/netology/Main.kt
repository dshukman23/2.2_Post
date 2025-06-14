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

// ========== Класс Comment ==========
data class Comment(
    val id: Int,
    val fromId: Int,
    val date: Long,
    val text: String,
    val donut: Donut? = null,
    val replyToUser: Int? = null,
    val replyToComment: Int? = null,
    val attachments: Array<Attachment>? = null,
    val parentsStack: List<Int>? = null,
    val thread: Thread? = null
)

data class Donut(
    val isDon: Boolean,
    val placeholder: String
)

data class Thread(
    val count: Int,
    val items: List<Comment>?,
    val canPost: Boolean,
    val showReplyButton: Boolean,
    val groupsCanPost: Boolean
)

// ========== WallService для работы с постами ==========
class WallService {
    private var posts = emptyArray<Post>()
    private var comments = emptyArray<Comment>()
    private var nextId = 1

    fun add(post: Post): Post {
        val newPost = post.copy(id = nextId++)
        posts += newPost
        return newPost
    }

    fun update(post: Post): Boolean {
        for ((index, p) in posts.withIndex()) {
            if (p.id == post.id) {
                posts = posts.toMutableList().apply {
                    set(index, post)
                }.toTypedArray()
                return true
            }
        }
        return false
    }

    // ========== метод для создания комментария ==========
    fun createComment(postId: Int, comment: Comment): Comment {
        if (!posts.any { it.id == postId }) {
            throw PostNotFoundException("Пост с ID $postId не найден")
        }
        comments += comment
        return comment
    }

    fun clear() {
        posts = emptyArray()
        comments = emptyArray()
        nextId = 1
    }

    fun getPosts(): Array<Post> = posts
}

// ========== Собственное исключение ==========
class PostNotFoundException(message: String) : Exception(message)

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

    // Создаем комментарий
    val comment = Comment(
        id = 1,
        fromId = 200,
        date = System.currentTimeMillis() / 1000,
        text = "Отличное видео!"
    )

    try {
        val addedComment = wallService.createComment(savedPost.id, comment)
        println("Комментарий создан: $addedComment")
    } catch (e: PostNotFoundException) {
        println("Ошибка: ${e.message}")
    }
}