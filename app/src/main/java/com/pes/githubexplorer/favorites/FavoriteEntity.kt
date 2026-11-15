package com.pes.githubexplorer.favorites

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pes.githubexplorer.GitHubRepository

@Entity(tableName = "favorite_repositories")
data class FavoriteEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val fullName: String,
    val htmlUrl: String,
    val description: String?,
    val language: String?,
    val stars: Int,
    val forks: Int
) {
    fun toGitHubRepository() = GitHubRepository(
        id = id,
        name = name,
        fullName = fullName,
        htmlUrl = htmlUrl,
        description = description,
        language = language,
        stars = stars,
        forks = forks
    )

    companion object {
        fun from(repository: GitHubRepository) = FavoriteEntity(
            id = repository.id,
            name = repository.name,
            fullName = repository.fullName,
            htmlUrl = repository.htmlUrl,
            description = repository.description,
            language = repository.language,
            stars = repository.stars,
            forks = repository.forks
        )
    }
}
