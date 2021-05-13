# Pong game by Guillem Pétriz

import random
import time
import pygame

# definim els valors de la finestra del joc
WIDTH = 1200
HEIGHT = 600
BORDER = 50
VELOCITY = 10
FRAMERATE = 60

# Definim les classes


class Ball:  # classe que sencarrega de la bola
    RADIUS = 20

    def __init__(self, xcoord, ycoord, xspeed, yspeed):
        self.xcoord = xcoord  # coordenada x de la bola
        self.ycoord = ycoord  # coordenada y de la bola
        self.xspeed = xspeed
        self.yspeed = yspeed

    def show(self, color):  # Dibuixa la bola
        pygame.draw.circle(
            window, color, (self.xcoord, self.ycoord), self.RADIUS)

    def update(self):
        global backcolor, maincolor, ScoreUser, ScoreAI, scored
        # testegem si ens sortim dels marges de la taula
        nextxcoord = self.xcoord+self.xspeed
        nextycoord = self.ycoord+self.yspeed
        if self.xcoord < -Ball.RADIUS:
            # la bola surt per l'esquerra
            scored = True
            youLose.play()
        elif self.xcoord > WIDTH+Ball.RADIUS:
            # la bola surt per la dreta
            scored = True
            youWin.play()
        else:
            pass
        if nextycoord < BORDER+self.RADIUS or nextycoord > HEIGHT-BORDER-self.RADIUS:
            self.yspeed = -self.yspeed  # invertim direccio y
            bounce.play()
        elif nextxcoord < Paddle.WIDTH and abs(nextycoord-paddleAI.ycoord) < Paddle.HEIGHT/2:
            self.xspeed = -self.xspeed - \
                random.uniform(0.1, 0.5)  # invertim direccio X
            bouncePaddle.play()
        elif nextxcoord+Ball.RADIUS > WIDTH-Paddle.WIDTH and abs(nextycoord-paddleplay.ycoord) < Paddle.HEIGHT/2:
            # Si toca amb el paddle, tambe ha de rebotar!
            self.xspeed = -self.xspeed-random.uniform(0.1, 0.5)
            bouncePaddle.play()
            # canviem la velocitat de la bola cada cop que rebota de manera aleatoria
            if self.yspeed > 0:
                self.yspeed = self.yspeed+random.uniform(0.1, 0.5)
            else:
                self.yspeed = self.yspeed-random.uniform(0.1, 0.5)
        else:
            # actualitzem la bola
            self.show(backcolor)
            self.xcoord = self.xcoord+self.xspeed
            self.ycoord = self.ycoord+self.yspeed
            self.show(maincolor)


class Paddle:  # Classe que sencarrega de la pala
    HEIGHT = 100
    WIDTH = 20
    AI = False

    # AI es un boolea que defineix si la pala es controlada per la AI
    def __init__(self, ycoord, AI):
        self.ycoord = ycoord
        self.AI = AI

    # Ensenyem ambdues pales
    def show(self, color):
        if not self.AI:
            pygame.draw.rect(window, color, pygame.Rect(
                WIDTH-self.WIDTH, self.ycoord-self.HEIGHT/2, self.WIDTH, self.HEIGHT))
        else:
            pygame.draw.rect(window, color, pygame.Rect(
                0, self.ycoord-self.HEIGHT/2, self.WIDTH, self.HEIGHT))

    # Per a entrenar la AI, movem nosaltres el ratolí per a manejar la pala esquerra. La dreta segueix la bola

    def update(self):
        nextycoord = ballplay.ycoord
        if not self.AI:
            if nextycoord-self.HEIGHT/2 > BORDER and nextycoord+self.HEIGHT/2 < HEIGHT-BORDER:
                self.show(backcolor)
                self.ycoord = nextycoord
                self.show(maincolor)
        else:
            nextycoord = pygame.mouse.get_pos()[1]
            if nextycoord-self.HEIGHT/2 > BORDER and nextycoord+self.HEIGHT/2 < HEIGHT-BORDER:
                self.show(backcolor)
                self.ycoord = nextycoord
                self.show(maincolor)


# creem i inicialitzem els objectes
ballplay = Ball(WIDTH-Paddle.WIDTH-Ball.RADIUS, HEIGHT/2, -VELOCITY, -VELOCITY)
paddleplay = Paddle(HEIGHT/2, False)
paddleAI = Paddle(HEIGHT/2, True)

# Inicialitzem pygame
pygame.init()

window = pygame.display.set_mode((WIDTH, HEIGHT))  # Creem la finestra del joc
maincolor = pygame.Color("white")  # definim el color general del joc
backcolor = pygame.Color("Black")  # definim el color del background del joc
AIcolor = pygame.Color("Red")

# inicialitzem els sorolls de victoria, derrota, rebot...
bounce = pygame.mixer.Sound("bounce.wav")
bouncePaddle = pygame.mixer.Sound("bounce2.wav")
youLose = pygame.mixer.Sound("YouLose.wav")
youWin = pygame.mixer.Sound("YouWin.wav")

# inicialitzem les variables de la puntuacio
scored = False

# dibuixem els rectangles que ens serviran com a marges del nostre joc
pygame.draw.rect(window, maincolor, pygame.Rect((0, 0), (WIDTH, BORDER)))
#pygame.draw.rect(window, maincolor, pygame.Rect((0, 0, BORDER, HEIGHT)))
pygame.draw.rect(window, maincolor, pygame.Rect(
    0, HEIGHT-BORDER, WIDTH, BORDER))

# dibuixem lletres amb el marcador i tal
font = pygame.font.SysFont('Comic Sans MS', 30)
text = font.render("Training AI...", True, AIcolor)
window.blit(text, (WIDTH/2-Paddle.WIDTH*4, BORDER/4))

# mostrem les pales i la pilota
ballplay.show(maincolor)
paddleplay.show(maincolor)
paddleAI.show(AIcolor)

# inicialitzem el rellotge
clock = pygame.time.Clock()

# AI Training: Recopilem informacio en un arxiu de dades csv
sample = open("game.csv", "w")
print("xcoord,ycoord,xspeed,yspeed,paddleplay", file=sample)

# booleans del bucle del joc
train = True
first = True

while train:
    if first:
        time.sleep(5)
    first = False

    # tanquem el joc si es clica a la creu de la finestra
    event = pygame.event.poll()
    if event.type == pygame.QUIT:
        break

    # tic tac
    clock.tick(FRAMERATE)

    # mostrem els rectangles
    pygame.display.flip()

    # refresquem la posicio de la pala i de la bola
    paddleplay.update()
    ballplay.update()
    paddleAI.update()

    # imprimim cada moviment en el csv
    print("{},{},{},{},{}".format(ballplay.xcoord, ballplay.ycoord,
          ballplay.xspeed, ballplay.yspeed, paddleAI.ycoord), file=sample)

    # si ens marquen, s'acaba la sessio dentrenament
    if scored:
        time.sleep(5)
        train = False
