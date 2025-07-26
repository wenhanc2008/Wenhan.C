import 'dart:io';
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_storage/firebase_storage.dart';

class NewPostPage extends StatefulWidget {
  @override
  State<NewPostPage> createState() => _NewPostPageState();
}

class _NewPostPageState extends State<NewPostPage> {
  final _titleController = TextEditingController();
  final _priceController = TextEditingController();
  final _descController = TextEditingController();

  final List<XFile> _images = [];
  final ImagePicker _picker = ImagePicker();

  Future<void> _pickImage() async {
    if (_images.length >= 4) return;

    final picked = await _picker.pickImage(source: ImageSource.gallery);
    if (picked != null) {
      setState(() {
        _images.add(picked);
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('HyperGarageSale')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            TextField(
              controller: _titleController,
              decoration: InputDecoration(labelText: 'Enter title of the item'),
            ),
            SizedBox(height: 12),
            TextField(
              controller: _priceController,
              keyboardType: TextInputType.number,
              decoration: InputDecoration(labelText: 'Enter price'),
            ),
            SizedBox(height: 12),
            TextField(
              controller: _descController,
              decoration: InputDecoration(labelText: 'Enter description of the item'),
              maxLines: 3,
            ),
            SizedBox(height: 12),

            // 图片缩略图显示区域
            Wrap(
              spacing: 8,
              runSpacing: 8,
              children: _images.map((img) {
                return Stack(
                  children: [
                    Image.file(
                      File(img.path),
                      width: 80,
                      height: 80,
                      fit: BoxFit.cover,
                    ),
                    Positioned(
                      right: 0,
                      top: 0,
                      child: GestureDetector(
                        onTap: () {
                          setState(() {
                            _images.remove(img);
                          });
                        },
                        child: CircleAvatar(
                          radius: 10,
                          backgroundColor: Colors.black,
                          child: Icon(Icons.close, size: 14, color: Colors.white),
                        ),
                      ),
                    )
                  ],
                );
              }).toList(),
            ),
            SizedBox(height: 12),

            OutlinedButton.icon(
              onPressed: _pickImage,
              icon: Icon(Icons.add_a_photo),
              label: Text('Add Image (${_images.length}/4)'),
            ),

            Spacer(),

            ElevatedButton(
              onPressed: () async {
                final title = _titleController.text.trim();
                final price = _priceController.text.trim();
                final desc = _descController.text.trim();

                if (title.isEmpty || price.isEmpty || desc.isEmpty) {
                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(content: Text('Please fill in all fields')),
                  );
                  return;
                }

                try {
                  // 🔼 上传图片到 Firebase Storage
                  List<String> imageUrls = [];

                  for (var img in _images) {
                    final file = File(img.path);
                    final fileName = DateTime.now().millisecondsSinceEpoch.toString();
                    final ref = FirebaseStorage.instance
                        .ref()
                        .child('post_images')
                        .child('$fileName.jpg');

                    await ref.putFile(file);
                    final url = await ref.getDownloadURL();
                    imageUrls.add(url);
                  }

                  // 🧾 写入 Firestore 数据
                  await FirebaseFirestore.instance.collection('posts').add({
                    'title': title,
                    'price': price,
                    'description': desc,
                    'createdAt': Timestamp.now(),
                    'imageUrls': imageUrls,
                  });

                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(content: Text('✅ Posted with ${imageUrls.length} image(s)!')),
                  );

                  _titleController.clear();
                  _priceController.clear();
                  _descController.clear();
                  setState(() {
                    _images.clear();
                  });
                } catch (e) {
                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(content: Text('Error: $e')),
                  );
                }
              },
              child: Text('POST'),
            ),
          ],
        ),
      ),
    );
  }
}