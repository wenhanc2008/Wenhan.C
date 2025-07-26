import 'package:flutter/material.dart';

class PostDetailPage extends StatefulWidget {
  final String title;
  final String price;
  final String description;
  final List<String> imageUrls;

  const PostDetailPage({
    required this.title,
    required this.price,
    required this.description,
    required this.imageUrls,
  });

  @override
  State<PostDetailPage> createState() => _PostDetailPageState();
}

class _PostDetailPageState extends State<PostDetailPage> {
  int _currentPage = 0;

  void _goToPrevious() {
    if (_currentPage > 0) {
      setState(() {
        _currentPage--;
      });
    }
  }

  void _goToNext() {
    if (_currentPage < widget.imageUrls.length - 1) {
      setState(() {
        _currentPage++;
      });
    }
  }

  Widget _buildPageIndicator(int count) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: List.generate(
        count,
        (index) => AnimatedContainer(
          duration: Duration(milliseconds: 300),
          margin: EdgeInsets.symmetric(horizontal: 4),
          width: _currentPage == index ? 12 : 8,
          height: _currentPage == index ? 12 : 8,
          decoration: BoxDecoration(
            shape: BoxShape.circle,
            color: _currentPage == index ? Colors.deepPurple : Colors.grey,
          ),
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final imageCount = widget.imageUrls.length;

    return Scaffold(
      appBar: AppBar(title: Text('Post Details')),
      body: SingleChildScrollView(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // ✅ 图片轮播区域（按钮控制）
              if (imageCount > 0)
                Stack(
                  alignment: Alignment.center,
                  children: [
                    ClipRRect(
                      borderRadius: BorderRadius.circular(12),
                      child: Image.network(
                        widget.imageUrls[_currentPage],
                        width: double.infinity,
                        fit: BoxFit.contain,
                      ),
                    ),
                    // ← 左按钮
                    Positioned(
                      left: 0,
                      child: IconButton(
                        icon: Icon(Icons.arrow_back_ios, size: 30),
                        color: _currentPage > 0 ? Colors.black : Colors.grey[300],
                        onPressed: _goToPrevious,
                      ),
                    ),
                    // → 右按钮
                    Positioned(
                      right: 0,
                      child: IconButton(
                        icon: Icon(Icons.arrow_forward_ios, size: 30),
                        color: _currentPage < imageCount - 1 ? Colors.black : Colors.grey[300],
                        onPressed: _goToNext,
                      ),
                    ),
                  ],
                )
              else
                Container(
                  height: 200,
                  alignment: Alignment.center,
                  decoration: BoxDecoration(
                    color: Colors.grey[200],
                    borderRadius: BorderRadius.circular(12),
                  ),
                  child: Icon(Icons.image_not_supported, size: 64, color: Colors.grey),
                ),

              SizedBox(height: 12),
              _buildPageIndicator(imageCount),

              SizedBox(height: 20),

              // ✅ 信息卡片区域
              Container(
                width: double.infinity,
                padding: EdgeInsets.all(16),
                decoration: BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.circular(12),
                  boxShadow: [
                    BoxShadow(
                      color: Colors.black12,
                      blurRadius: 6,
                      offset: Offset(0, 3),
                    ),
                  ],
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      widget.title,
                      style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold, color: Colors.deepPurple),
                    ),
                    SizedBox(height: 8),
                    Text(
                      '\$${widget.price}',
                      style: TextStyle(fontSize: 20, color: Colors.green[700], fontWeight: FontWeight.w500),
                    ),
                    SizedBox(height: 12),
                    Text(widget.description, style: TextStyle(fontSize: 16)),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}