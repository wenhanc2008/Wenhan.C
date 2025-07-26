import 'package:flutter/material.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'new_post_page.dart';
import 'post_detail_page.dart';

class BrowsePostsPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Browse Posts'),
        actions: [
          PopupMenuButton<String>(
            onSelected: (value) {
              if (value == 'post') {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (_) => NewPostPage()),
                );
              }
            },
            itemBuilder: (context) => [
              PopupMenuItem(
                value: 'post',
                child: Text('Post New Item'),
              ),
            ],
          ),
        ],
      ),
      body: StreamBuilder<QuerySnapshot>(
        stream: FirebaseFirestore.instance
            .collection('posts')
            .orderBy('createdAt', descending: true)
            .snapshots(),
        builder: (context, snapshot) {
          if (snapshot.hasError) {
            return Center(child: Text('Something went wrong'));
          }

          if (snapshot.connectionState == ConnectionState.waiting) {
            return Center(child: CircularProgressIndicator());
          }

          final docs = snapshot.data!.docs;

          if (docs.isEmpty) {
            return Center(child: Text('No posts yet!'));
          }

          return ListView.builder(
            itemCount: docs.length,
            itemBuilder: (context, index) {
              final post = docs[index];
              final title = post['title'] ?? '';
              final price = post['price'] ?? '';
              final desc = post['description'] ?? '';
              final imageUrls = List<String>.from(post['imageUrls'] ?? []);

              return Card(
                margin: EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                child: ListTile(
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => PostDetailPage(
                          title: title,
                          price: price,
                          description: desc,
                          imageUrls: imageUrls,
                        ),
                      ),
                    );
                  },
                  leading: imageUrls.isNotEmpty
                      ? Image.network(
                          imageUrls[0],
                          width: 60,
                          height: 60,
                          fit: BoxFit.cover,
                        )
                      : Icon(Icons.image_not_supported, size: 40),
                  title: Text('$title - \$$price'),
                  subtitle: Text(desc),
                ),
              );
            },
          );
        },
      ),
    );
  }
}