package ru.netology

// Класс для комментариев
data class Comments(
    val count: Int = 0,
    val canPost: Boolean = true,
    val groupsCanPost: Boolean = false,
    val canClose: Boolean = false,
    val canOpen: Boolean = false
)

// Класс для лайков
data class Likes(
    val count: Int = 0,
    val userLikes: Boolean = false,
    val canLike: Boolean = true,
    val canPublish: Boolean = true
)

// Основной класс поста
data class Post(
    val id: Int,
    val ownerId: Int,
    val fromId: Int,
    val date: Long,
    var text: String,
    val friendsOnly: Boolean = false,
    val comments: Comments = Comments(),
    val likes: Likes = Likes(),
    val viewsCount: Int = 0,
    val postType: String = "post",
    val canPin: Boolean = false,
    val canDelete: Boolean = false,
    val canEdit: Boolean = false,
    val isPinned: Boolean = false,
    val markedAsAds: Boolean = false,
    val isFavorite: Boolean = false
)

class WallService {
    private val posts = mutableListOf<Post>()
    private var nextId = 1

    // Основной метод добавления поста
    fun add(post: Post): Post {
        val newPost = post.copy(id = nextId++)
        posts.add(newPost)
        return newPost
    }

    // Метод обновления поста
    fun update(post: Post): Boolean {
        val index = posts.indexOfFirst { it.id == post.id }
        if (index != -1) {
            posts[index] = post
            return true
        }
        return false
    }

    // Для тестирования
    fun clear() {
        posts.clear()
        nextId = 1
    }

    fun getPosts(): List<Post> = posts
}

// Пример использования
fun main() {
    val wallService = WallService()

    val originalPost = Post(
        id = 0,
        ownerId = 100,
        fromId = 100,
        date = System.currentTimeMillis() / 1000,
        text = "Привет, это мой первый пост!",
        comments = Comments(count = 5),
        likes = Likes(count = 10, userLikes = true)
    )

    val savedPost = wallService.add(originalPost)
    println("Сохранённый пост: $savedPost")

    // Обновляем текст
    val updatedPost = savedPost.copy(text = "Обновлённый текст!")
    val isUpdated = wallService.update(updatedPost)
    println("Пост обновлён: $isUpdated")

    val allPosts = wallService.getPosts()
    println("Все посты: $allPosts")
}