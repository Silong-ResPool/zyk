const canvas = document.getElementById('gameCanvas');
const ctx = canvas.getContext('2d');

let gameRunning = false;
let keys = {};
let player1, player2;

class Mecha {
    constructor(x, y, color, isPlayer1) {
        this.x = x;
        this.y = y;
        this.width = 64;
        this.height = 80;
        this.hp = 100;
        this.maxHp = 100;
        this.speed = 4;
        this.color = color;
        this.facing = isPlayer1 ? 1 : -1;
        this.isPlayer1 = isPlayer1;
        this.isAttacking = false;
        this.isDefending = false;
        this.attackCooldown = 0;
        this.defendCooldown = 0;
        this.animFrame = 0;
        this.attackBox = null;
        this.hitFlash = 0;
    }

    update() {
        if (this.attackCooldown > 0) this.attackCooldown--;
        if (this.defendCooldown > 0) this.defendCooldown--;
        if (this.hitFlash > 0) this.hitFlash--;
        
        this.animFrame = (this.animFrame + 0.15) % 4;

        if (!this.isAttacking) {
            this.attackBox = null;
        }

        const minX = 0;
        const maxX = canvas.width - this.width;
        const minY = 100;
        const maxY = canvas.height - this.height;

        this.x = Math.max(minX, Math.min(maxX, this.x));
        this.y = Math.max(minY, Math.min(maxY, this.y));
    }

    draw() {
        ctx.save();
        
        if (this.hitFlash > 0 && Math.floor(this.hitFlash / 3) % 2 === 0) {
            ctx.globalAlpha = 0.5;
        }

        const drawX = this.x;
        const drawY = this.y;

        if (this.facing === -1) {
            ctx.translate(drawX + this.width, drawY);
            ctx.scale(-1, 1);
            this.drawMecha(0, 0);
        } else {
            this.drawMecha(drawX, drawY);
        }

        ctx.restore();

        if (this.attackBox && this.isAttacking) {
            ctx.fillStyle = 'rgba(255, 255, 0, 0.3)';
            ctx.fillRect(this.attackBox.x, this.attackBox.y, this.attackBox.width, this.attackBox.height);
        }

        if (this.isDefending) {
            ctx.save();
            ctx.strokeStyle = '#fff';
            ctx.lineWidth = 4;
            ctx.setLineDash([8, 4]);
            ctx.beginPath();
            ctx.arc(this.x + this.width/2, this.y + this.height/2, 50, 0, Math.PI * 2);
            ctx.stroke();
            ctx.restore();
        }
    }

    drawMecha(x, y) {
        const pixelSize = 4;
        const px = (size) => size * pixelSize;

        const bodyColor = this.color;
        const darkColor = this.isPlayer1 ? '#8b0000' : '#006666';
        const lightColor = this.isPlayer1 ? '#ff6b6b' : '#7fefef';

        ctx.fillStyle = bodyColor;
        this.drawPixelRect(x + px(4), y + px(6), px(8), px(10));

        ctx.fillStyle = lightColor;
        this.drawPixelRect(x + px(5), y, px(6), px(6));

        ctx.fillStyle = '#000';
        this.drawPixelRect(x + px(7), y + px(2), px(2), px(2));

        ctx.fillStyle = darkColor;
        if (this.isAttacking) {
            this.drawPixelRect(x + px(12), y + px(7), px(6), px(3));
        } else {
            this.drawPixelRect(x + px(11), y + px(7), px(4), px(3));
        }
        this.drawPixelRect(x + px(1), y + px(7), px(3), px(3));

        const legOffset = Math.floor(this.animFrame) % 2 === 0 ? 0 : px(1);
        ctx.fillStyle = darkColor;
        this.drawPixelRect(x + px(5), y + px(16) - legOffset, px(2), px(4));
        this.drawPixelRect(x + px(9), y + px(16) + legOffset, px(2), px(4));

        ctx.fillStyle = '#666';
        this.drawPixelRect(x + px(4), y + px(20) - legOffset, px(4), px(1));
        this.drawPixelRect(x + px(8), y + px(20) + legOffset, px(4), px(1));
    }

    drawPixelRect(x, y, w, h) {
        ctx.fillRect(Math.floor(x), Math.floor(y), w, h);
    }

    attack() {
        if (this.attackCooldown > 0) return;
        
        this.isAttacking = true;
        this.attackCooldown = 40;

        const attackWidth = 50;
        const attackHeight = 60;
        this.attackBox = {
            x: this.facing === 1 ? this.x + this.width : this.x - attackWidth,
            y: this.y + 10,
            width: attackWidth,
            height: attackHeight
        };

        setTimeout(() => {
            this.isAttacking = false;
        }, 200);
    }

    defend() {
        if (this.defendCooldown > 0) return;
        
        this.isDefending = true;
        this.defendCooldown = 60;

        setTimeout(() => {
            this.isDefending = false;
        }, 500);
    }

    takeDamage(damage) {
        if (this.isDefending) {
            damage = Math.floor(damage * 0.3);
        }
        this.hp = Math.max(0, this.hp - damage);
        this.hitFlash = 20;
        updateHealthBars();
    }

    moveUp() {
        this.y -= this.speed;
    }

    moveDown() {
        this.y += this.speed;
    }

    moveLeft() {
        this.x -= this.speed;
        this.facing = -1;
    }

    moveRight() {
        this.x += this.speed;
        this.facing = 1;
    }
}

function initGame() {
    player1 = new Mecha(100, 300, '#e94560', true);
    player2 = new Mecha(636, 300, '#4ecdc4', false);
}

function drawBackground() {
    const gradient = ctx.createLinearGradient(0, 0, 0, canvas.height);
    gradient.addColorStop(0, '#1a1a2e');
    gradient.addColorStop(1, '#0f3460');
    ctx.fillStyle = gradient;
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    ctx.fillStyle = '#fff';
    for (let i = 0; i < 30; i++) {
        const x = (i * 73) % canvas.width;
        const y = (i * 47) % 200;
        const size = (i % 3) + 1;
        ctx.fillRect(x, y, size, size);
    }

    ctx.fillStyle = '#2d4a3e';
    ctx.fillRect(0, canvas.height - 60, canvas.width, 60);

    ctx.fillStyle = '#3d6a5e';
    for (let i = 0; i < canvas.width; i += 16) {
        ctx.fillRect(i, canvas.height - 60, 8, 4);
    }

    ctx.fillStyle = '#4a6a7d';
    for (let i = 0; i < canvas.width; i += 40) {
        ctx.fillRect(i, canvas.height - 56, 24, 8);
    }
}

function checkCollision(box1, box2) {
    return box1.x < box2.x + box2.width &&
           box1.x + box1.width > box2.x &&
           box1.y < box2.y + box2.height &&
           box1.y + box1.height > box2.y;
}

function handleInput() {
    if (keys['w'] || keys['W']) player1.moveUp();
    if (keys['s'] || keys['S']) player1.moveDown();
    if (keys['a'] || keys['A']) player1.moveLeft();
    if (keys['d'] || keys['D']) player1.moveRight();
    
    if (keys['ArrowUp']) player2.moveUp();
    if (keys['ArrowDown']) player2.moveDown();
    if (keys['ArrowLeft']) player2.moveLeft();
    if (keys['ArrowRight']) player2.moveRight();
}

function gameLoop() {
    if (!gameRunning) return;

    drawBackground();
    handleInput();

    player1.update();
    player2.update();

    if (player1.attackBox) {
        const p2Box = { x: player2.x, y: player2.y, width: player2.width, height: player2.height };
        if (checkCollision(player1.attackBox, p2Box)) {
            player2.takeDamage(15);
            player1.attackBox = null;
        }
    }

    if (player2.attackBox) {
        const p1Box = { x: player1.x, y: player1.y, width: player1.width, height: player1.height };
        if (checkCollision(player2.attackBox, p1Box)) {
            player1.takeDamage(15);
            player2.attackBox = null;
        }
    }

    player1.draw();
    player2.draw();

    if (player1.hp <= 0) {
        endGame('玩家2 获胜！');
        return;
    }
    if (player2.hp <= 0) {
        endGame('玩家1 获胜！');
        return;
    }

    requestAnimationFrame(gameLoop);
}

function updateHealthBars() {
    const health1Fill = document.getElementById('health1');
    const health2Fill = document.getElementById('health2');
    const health1Text = document.getElementById('healthText1');
    const health2Text = document.getElementById('healthText2');

    const p1Percent = (player1.hp / player1.maxHp) * 100;
    const p2Percent = (player2.hp / player2.maxHp) * 100;

    health1Fill.style.width = p1Percent + '%';
    health2Fill.style.width = p2Percent + '%';
    health1Text.textContent = `${player1.hp}/${player1.maxHp}`;
    health2Text.textContent = `${player2.hp}/${player2.maxHp}`;
}

function startGame() {
    document.getElementById('startOverlay').classList.add('hidden');
    initGame();
    gameRunning = true;
    gameLoop();
}

function endGame(winnerText) {
    gameRunning = false;
    document.getElementById('winnerText').textContent = '🏆 ' + winnerText + ' 🏆';
    document.getElementById('endOverlay').classList.remove('hidden');
}

function restartGame() {
    document.getElementById('endOverlay').classList.add('hidden');
    initGame();
    updateHealthBars();
    gameRunning = true;
    gameLoop();
}

document.addEventListener('keydown', (e) => {
    keys[e.key] = true;

    if (e.key === 'q' || e.key === 'Q') {
        player1.attack();
    }
    if (e.key === 'e' || e.key === 'E') {
        player1.defend();
    }
    if (e.key === 'j' || e.key === 'J') {
        player2.attack();
    }
    if (e.key === 'k' || e.key === 'K') {
        player2.defend();
    }

    if (['ArrowUp', 'ArrowDown', 'ArrowLeft', 'ArrowRight', ' '].includes(e.key)) {
        e.preventDefault();
    }
});

document.addEventListener('keyup', (e) => {
    keys[e.key] = false;
});

drawBackground();
initGame();
